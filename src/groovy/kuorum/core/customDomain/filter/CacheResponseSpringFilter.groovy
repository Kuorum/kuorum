package kuorum.core.customDomain.filter

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.register.KuorumUserSession
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
    SpringSecurityService springSecurityService
    UrlMappingsHolder urlMappingsHolder

    public static final String HEADER_LAST_MODIFIED = "Last-Modified";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_EXPIRES = "Expires";
    public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";

    private static final String REQUEST_FILTERED = "cache_filter_" + CacheResponseSpringFilter.class.getName();

    // TODO: CHANGE CACHE
    HashMap<String, ResponseContent> fakeCache = new HashMap<>();

    // Last Modified parameter
    public static final long LAST_MODIFIED_INITIAL = -1;

    // Expires parameter
    public static final long EXPIRES_ON = 1;
    public static final long MAX_AGE_TIME = -60;

    private int time = 60 * 60;
    private long lastModified = LAST_MODIFIED_INITIAL;
    private long expires = EXPIRES_ON;
    private long cacheControlMaxAge = MAX_AGE_TIME;

    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request

        if (isCacheable(httpServletRequest)) {
            // Evita llamadas repetidas
            if (isFilteredBefore(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            request.setAttribute(REQUEST_FILTERED, Boolean.TRUE);

            URL url = new URL(httpServletRequest.getRequestURL().toString())
            String key = buildKey(url);
            ResponseContent responseContent = fakeCache.get(key);
            if (responseContent != null) {
                responseContent.writeTo(response);
                return;
            }

            CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper((HttpServletResponse) response,
                    time * 1000L, lastModified, expires, cacheControlMaxAge);
            filterChain.doFilter(request, cacheResponse);
            cacheResponse.flushBuffer();
            // Store as the cache content the result of the response
            fakeCache.put(key, cacheResponse.getContent());
        } else {
            filterChain.doFilter(request, response);
        }

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

    String buildKey(URL url) {
        if (springSecurityService.isLoggedIn()) {
            return url.toString() + ((KuorumUserSession) springSecurityService.principal).id
        } else {
            return url.toString();
        }
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
