package kuorum.core.customDomain.filter

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.navigation.cache.ServletRequestResponseCache
import kuorum.web.constants.WebConstants
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo
import org.codehaus.groovy.grails.web.mapping.UrlMappingsHolder
import org.springframework.web.servlet.i18n.CookieLocaleResolver

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static java.lang.Boolean.parseBoolean
import static org.apache.commons.lang.LocaleUtils.toLocale

class CacheResponseSpringFilter extends AbstractWrappedResponseFilter {

    public static final String CACHE_ACTIVE = "cacheActive"
    UrlMappingsHolder urlMappingsHolder
    ServletRequestResponseCache servletResponseCache
    SpringSecurityService springSecurityService

    private static final String REQUEST_FILTERED = "cache_filter_" + CacheResponseSpringFilter.class.getName()


    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = request as HttpServletRequest


        UrlMappingInfo urlMappingInfo = getUrlMapping(httpServletRequest)
        if (isCacheable(urlMappingInfo)) {
            // Evita llamadas repetidas
            if (isFilteredBefore(request)) {
                filterChain.doFilter(request, response)
                return
            }
            request.setAttribute(REQUEST_FILTERED, Boolean.TRUE)

            Locale locale = getLocale(httpServletRequest, response)

            if (!servletResponseCache.get(urlMappingInfo, response, locale)) {
                servletResponseCache.put(urlMappingInfo, wrapResponse(request, response, filterChain), locale)
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }

    private UrlMappingInfo getUrlMapping(HttpServletRequest httpServletRequest) {
        urlMappingsHolder.matchAll(httpServletRequest.forwardURI.replaceFirst(httpServletRequest.contextPath, "")).find {it.actionName }
    }

    private Boolean isCacheable(UrlMappingInfo urlMappingInfo) {
        return !springSecurityService.isLoggedIn() && isUriCacheable(urlMappingInfo)
    }

    private boolean isUriCacheable(UrlMappingInfo urlMappingInfo) {
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

    Locale getLocale(HttpServletRequest request, HttpServletResponse response) {
        String urlParamLang = request.getParameter(WebConstants.WEB_PARAM_LANG)
        if (urlParamLang) {
            updateCookieIfNeeded(request, response, urlParamLang)
            return toLocale(urlParamLang)
        }
        String cookieLocaleString = lookForCookieLocale(request)
        if (cookieLocaleString) {
            return toLocale(cookieLocaleString)
        }
        return request.getLocale()
    }

    private void updateCookieIfNeeded(HttpServletRequest request, HttpServletResponse response, String urlParamLang) {
        Cookie cookie = getLocaleCookie(request)
        if (cookie && !cookie.getValue().startsWith(urlParamLang)) {
            cookie.setValue(urlParamLang)
            response.addCookie(cookie)
        }
    }

    private String lookForCookieLocale(HttpServletRequest servletRequest) {
        getLocaleCookie(servletRequest)?.value
    }

    private Cookie getLocaleCookie(HttpServletRequest servletRequest) {
        ((HttpServletRequest) servletRequest).getCookies().find({ cookie -> cookie.name == CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME })
    }

}