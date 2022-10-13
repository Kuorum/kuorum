package kuorum.core.navigation.cache

import net.spy.memcached.MemcachedClient
import net.spy.memcached.internal.OperationFuture
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo

import javax.servlet.ServletResponse

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
    void put(UrlMappingInfo urlMappingInfo, CacheHttpServletResponseWrapper response, Locale locale) {
        def key = buildKey(urlMappingInfo, locale)
        memcachedClient.set(key, expirationInADay, response.getContent().getContent())

        String language = locale.getLanguage()

        def simpleKey = buildKey(urlMappingInfo)
        OperationFuture<Boolean> appendTry = memcachedClient.append(simpleKey, SEPARATOR + language)
        if (appendTry.getStatus().isSuccess()) {
            logger.debug("language ${language} append on ${simpleKey}")
        } else {
            memcachedClient.set(simpleKey, expirationInADay, language)
            logger.debug("language ${language} new cached on ${simpleKey}")
        }
        logger.debug("Key $key cached")
    }

    @Override
    boolean get(UrlMappingInfo urlMappingInfo, ServletResponse response, Locale locale) {
        String key = buildKey(urlMappingInfo, locale)
        byte[] cached = memcachedClient.get(key)
        if (cached == null) {
            return false
        }
        writeTo(response, cached)
        logger.debug("Key $key recovered from cache")
        return true
    }

    @Override
    void evictCampaign(UrlMappingInfo urlMappingInfo) {
        evictMultilanguageKey(buildKey(urlMappingInfo))
    }

    @Override
    void evictGlobal(UrlMappingInfo urlMappingInfo) {
        evictMultilanguageKey(buildGlobalKey(urlMappingInfo))

    }

    private void evictMultilanguageKey(String simpleKey) {
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
}
