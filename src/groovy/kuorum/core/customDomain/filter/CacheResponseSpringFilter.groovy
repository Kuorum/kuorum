package kuorum.core.customDomain.filter

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.navigation.cache.CacheHttpServletResponseWrapper
import kuorum.core.navigation.cache.ServletRequestResponseCache
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo
import org.codehaus.groovy.grails.web.mapping.UrlMappingsHolder
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static java.lang.Boolean.parseBoolean

class CacheResponseSpringFilter extends AbstractWrappedResponseFilter {

    public static final String CACHE_ACTIVE = "cacheActive"
    UrlMappingsHolder urlMappingsHolder
    ServletRequestResponseCache servletResponseCache
    SpringSecurityService springSecurityService

    private static final String REQUEST_FILTERED = "cache_filter_" + CacheResponseSpringFilter.class.getName()


    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = request as HttpServletRequest

        if (isCacheable(httpServletRequest)) {
            // Evita llamadas repetidas
            if (isFilteredBefore(request)) {
                filterChain.doFilter(request, response)
                return
            }
            request.setAttribute(REQUEST_FILTERED, Boolean.TRUE)

            if (!servletResponseCache.get(httpServletRequest, response)) {
                servletResponseCache.put(httpServletRequest, wrapResponse(request, response, filterChain))
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }

    private Boolean isCacheable(HttpServletRequest httpServletRequest) {
        return !springSecurityService.isLoggedIn() && isUriCacheable(httpServletRequest)
    }

    private boolean isUriCacheable(HttpServletRequest httpServletRequest) {
        UrlMappingInfo urlMappingInfo = urlMappingsHolder.match(httpServletRequest.forwardURI.replaceFirst(httpServletRequest.contextPath, ""))
        return urlMappingInfo && urlMappingInfo.parameters.containsKey(CACHE_ACTIVE) && parseBoolean(urlMappingInfo.parameters.get(CACHE_ACTIVE) as String)
    }

    /**
     * Checks if the request was filtered before, so guarantees to be executed
     * once per request. You can override this methods to define a more specific
     * behaviour.
     *
     * @param request checks if the request was filtered before.
     * @return true if it is the first execution
     */
    boolean isFilteredBefore(ServletRequest request) {
        return request.getAttribute(REQUEST_FILTERED) != null
    }
}
