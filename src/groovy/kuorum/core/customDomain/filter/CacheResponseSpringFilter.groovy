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
import javax.servlet.http.HttpServletResponse

class CacheResponseSpringFilter extends GenericFilterBean {

    public static final String CACHE_ACTIVE = "cacheActive"
    UrlMappingsHolder urlMappingsHolder
    ServletResponseCache servletResponseCache

    private static final String REQUEST_FILTERED = "cache_filter_" + CacheResponseSpringFilter.class.getName();


    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request

        if (isCacheable(httpServletRequest)) {
            // Evita llamadas repetidas
            if (isFilteredBefore(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            request.setAttribute(REQUEST_FILTERED, Boolean.TRUE);

            URL url = new URL(httpServletRequest.getRequestURL().toString())

            if (!servletResponseCache.get(url, response)) {
                CacheHttpServletResponseWrapper cacheResponse = wrapResponse(request, response, filterChain)
                servletResponseCache.put(url, cacheResponse.getContent())
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private CacheHttpServletResponseWrapper wrapResponse(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper((HttpServletResponse) response);
        filterChain.doFilter(request, cacheResponse);
        cacheResponse.flushBuffer();
        return cacheResponse
    }

    private Boolean isCacheable(HttpServletRequest httpServletRequest) {
        UrlMappingInfo urlMappingInfo = urlMappingsHolder.match(httpServletRequest.forwardURI.replaceFirst(httpServletRequest.contextPath, ""))
        Boolean cacheable

        if (urlMappingInfo && urlMappingInfo.parameters.containsKey(CACHE_ACTIVE)) {
            cacheable = Boolean.parseBoolean(urlMappingInfo.parameters.get(CACHE_ACTIVE) as String)
        } else {
            cacheable = false
        }

        return cacheable;
    }

    /**
     * Checks if the request was filtered before, so guarantees to be executed
     * once per request. You can override this methods to define a more specific
     * behaviour.
     *
     * @param request checks if the request was filtered before.
     * @return true if it is the first execution
     */
    public boolean isFilteredBefore(ServletRequest request) {
        return request.getAttribute(REQUEST_FILTERED) != null;
    }

}
