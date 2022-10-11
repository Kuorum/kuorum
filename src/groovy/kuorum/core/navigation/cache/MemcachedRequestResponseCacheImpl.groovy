package kuorum.core.navigation.cache

import kuorum.web.constants.WebConstants
import net.spy.memcached.MemcachedClient
import net.spy.memcached.internal.OperationFuture
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.web.servlet.i18n.CookieLocaleResolver

import javax.servlet.ServletResponse
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static org.apache.commons.lang.LocaleUtils.toLocale

class MemcachedRequestResponseCacheImpl extends AbstractHttpRequestKeyCache {

    Integer expirationInADay = 24 * 60 * 60 * 1 // 1 days
    private final static String SEPARATOR = "&"

    protected final Log logger = LogFactory.getLog(getClass())

    MemcachedClient memcachedClient

    MemcachedRequestResponseCacheImpl() {}

    MemcachedRequestResponseCacheImpl(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient
    }

    @Override
    void put(HttpServletRequest request, CacheHttpServletResponseWrapper response) {
        def key = buildKey(request, getLocale(request, response))
        memcachedClient.set(key, expirationInADay, response.getContent().getContent())

        String language = getLocale(request,response).getLanguage()
        OperationFuture<Boolean> appendTry = memcachedClient.append(buildKey(request), SEPARATOR + language)
        if (appendTry.getStatus().isSuccess()) {
            logger.debug("language ${language} append")
        } else {
            memcachedClient.set(buildKey(request), expirationInADay, language)
            logger.debug("language ${language} new cached")
        }
        logger.debug("Key $key cached")
    }

    @Override
    boolean get(HttpServletRequest request, ServletResponse response) {
        String key = buildKey(request, getLocale(request, response))
        byte[] cached = memcachedClient.get(key)
        if (cached == null) {
            return false
        }
        writeTo(response, cached)
        logger.debug("Key $key recovered from cache")
        return true
    }

    @Override
    void evict(HttpServletRequest request) {
        String simpleKey = buildKey(request)
        String cachedLanguages = memcachedClient.get(simpleKey) as String
        if (cachedLanguages) {
            cachedLanguages.split(SEPARATOR).each {
                memcachedClient.delete(simpleKey + it)
                logger.debug("Key ${simpleKey + it} deleted cache")
            }
            memcachedClient.delete(simpleKey)
            logger.debug("Key ${simpleKey} deleted cache")
        }
    }

    private writeTo(ServletResponse response, byte[] content) {
        OutputStream out = new BufferedOutputStream(response.getOutputStream())
        response.setContentLength(content.length)
        out.write(content)
        out.flush()
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
