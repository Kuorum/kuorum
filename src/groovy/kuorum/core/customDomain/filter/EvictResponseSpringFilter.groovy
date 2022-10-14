package kuorum.core.customDomain.filter

import grails.converters.JSON
import kuorum.core.navigation.cache.CacheHttpServletResponseWrapper
import kuorum.core.navigation.cache.ServletRequestResponseCache
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import org.codehaus.groovy.grails.web.json.JSONElement
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo
import org.codehaus.groovy.grails.web.mapping.UrlMappingsHolder
import org.springframework.web.bind.annotation.RequestMethod

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import java.nio.charset.Charset

import static java.lang.Boolean.parseBoolean

class EvictResponseSpringFilter extends AbstractWrappedResponseFilter {

    public static final String CAMPAIGN_EVICT = "cacheCampaignEvict"
    public static final String GLOBAL_EVICT = "cacheGlobalEvict"
    public static final String AJAX_PREFIX = "/ajax"
    UrlMappingsHolder urlMappingsHolder
    ServletRequestResponseCache servletResponseCache

    private static final String REQUEST_FILTERED = "cache_filter_" + EvictResponseSpringFilter.class.getName()


    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        CacheHttpServletResponseWrapper wrappedResponse = wrapResponse(request, response, filterChain)
        HttpServletRequest httpServletRequest = request as HttpServletRequest

        //Avoid action on AJAX request fail
        if (httpServletRequest.getServletPath()?.startsWith(AJAX_PREFIX)) {
            try {
                JSONElement json = JSON.parse(new String(wrappedResponse.getContent().getContent(), Charset.defaultCharset()))
                if (json.success == false) {
                    logger.debug("Ajax not success response")
                    return
                }
            } catch (ConverterException e) {
                logger.debug("Ajax html response")
            } catch (MissingPropertyException e) {
                logger.debug("Ajax is not following success structure: Ajax call ->\n {}", new String(wrappedResponse.getContent().getContent(), Charset.defaultCharset()))
            }
        }

        // Avoid repeated calls
        if (isFilteredBefore(request)) {
            filterChain.doFilter(request, response)
            return
        }
        request.setAttribute(REQUEST_FILTERED, Boolean.TRUE)

        UrlMappingInfo urlMappingInfo = urlMappingsHolder.match(httpServletRequest.forwardURI.replaceFirst(httpServletRequest.contextPath, ""))
        String method = httpServletRequest.method
        if (haveEvict(urlMappingInfo, method, CAMPAIGN_EVICT)) {
            servletResponseCache.evictCampaign(urlMappingInfo)
        }
        if (haveEvict(urlMappingInfo, method, GLOBAL_EVICT)) {
            servletResponseCache.evictGlobal(urlMappingInfo)
        }
    }

    private Boolean haveEvict(UrlMappingInfo urlMappingInfo, method, String cacheParameter) {
        return urlMappingInfo && urlMappingInfo.parameters.containsKey(cacheParameter) && urlMappingInfo.parameters.get(cacheParameter) == method
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
