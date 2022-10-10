package kuorum.core.customDomain.filter


import kuorum.core.navigation.cache.ServletResponseCache
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo
import org.codehaus.groovy.grails.web.mapping.UrlMappingsHolder
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

import static java.lang.Boolean.parseBoolean

class EvictResponseSpringFilter extends GenericFilterBean {

    public static final String CACHE_EVICT = "cacheEvict"
    UrlMappingsHolder urlMappingsHolder
    ServletResponseCache servletResponseCache

    private static final String REQUEST_FILTERED = "cache_filter_" + EvictResponseSpringFilter.class.getName()


    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request, response)


        def httpServletRequest = request as HttpServletRequest
        if (haveEvict(httpServletRequest)) {
            // Evita llamadas repetidas TODO revisar
            if (isFilteredBefore(request)) {
                filterChain.doFilter(request, response)
                return
            }
            request.setAttribute(REQUEST_FILTERED, Boolean.TRUE)

            URL url = new URL(httpServletRequest.getRequestURL().toString())
            servletResponseCache.evict(url)
        }
    }

    private Boolean haveEvict(HttpServletRequest httpServletRequest) {
        UrlMappingInfo urlMappingInfo = urlMappingsHolder.match(httpServletRequest.forwardURI.replaceFirst(httpServletRequest.contextPath, ""))
        //TODO POST from constant
        return urlMappingInfo && httpServletRequest.method == "POST" && urlMappingInfo.parameters.containsKey(CACHE_EVICT) && parseBoolean(urlMappingInfo.parameters.get(CACHE_EVICT) as String)
    }

    /**
     * Checks if the request was filtered before, so guarantees to be executed
     * once per request. You can override this methods to define a more specific
     * behaviour.
     *
     * @param request checks if the request was filtered before.
     * @return true if it is the first execution
     */
    boolean isFilteredBefore(ServletRequest request) { //TODO push up to abstract
        return request.getAttribute(REQUEST_FILTERED) != null
    }
}
