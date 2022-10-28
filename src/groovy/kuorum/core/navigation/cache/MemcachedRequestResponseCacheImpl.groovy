package kuorum.core.navigation.cache

import kuorum.core.customDomain.filter.CacheResponseSpringFilter
import net.spy.memcached.MemcachedClient
import net.spy.memcached.internal.OperationFuture
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo

import javax.servlet.ServletResponse

class MemcachedRequestResponseCacheImpl extends AbstractHttpRequestKeyCache {

    Map<String, Integer> expirationTimes = [
            "LANDING" : 24 * 60 * 60 * 1, // 1 days
            "CAMPAIGN": 24 * 60 * 60 * 1, // 1 days
            "RANKING" : 3 // 3 seconds
    ]
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
        int expiration = getExpiration(urlMappingInfo)
        memcachedClient.set(key, expiration, response.getContent().getContent())
        logger.debug("Key $key cached")

        def simpleKey = buildKey(urlMappingInfo)
        cacheLanguageKeys(urlMappingInfo, simpleKey, locale)
        cacheParentRelation(urlMappingInfo, simpleKey)
    }

    private int getExpiration(UrlMappingInfo urlMappingInfo) {
        return expirationTimes.get(urlMappingInfo.parameters.get(CacheResponseSpringFilter.CACHE_ACTIVE))
    }

    private void cacheParentRelation(UrlMappingInfo urlMappingInfo, String key) {
        String parentKey = buildParentKey(urlMappingInfo)
        if (parentKey) {
            safetyAppendCache(urlMappingInfo, parentKey + PARENT_SUFIX, key)
        }
    }

    private void cacheLanguageKeys(UrlMappingInfo urlMappingInfo, String key, Locale locale) {
        safetyAppendCache(urlMappingInfo, key, locale.getLanguage())
    }

    private void safetyAppendCache(UrlMappingInfo urlMappingInfo, String simpleKey, String content) {
        String codeToStorage = SEPARATOR + content
        OperationFuture<Boolean> appendTry = memcachedClient.append(simpleKey, codeToStorage)
        if (appendTry.getStatus().isSuccess()) {
            logger.debug("SubValue ${content} append on ${simpleKey}")
        } else {
            memcachedClient.set(simpleKey, getExpiration(urlMappingInfo), content)
            logger.debug("Subvalue ${content} new cached on ${simpleKey}")
        }
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
        String key = buildKey(urlMappingInfo)
        evictMultilanguageKey(key)
        evictMultilanguageKey(buildParentKey(urlMappingInfo))
        evictChildren(key)
    }

    @Override
    void evictGlobal() {
        evictMultilanguageKey(buildGlobalKey())
    }

    private void evictChildren(String key) {
        def childrenKey = key + PARENT_SUFIX
        String cachedChildren = memcachedClient.get(childrenKey)
        if (cachedChildren) {
            cachedChildren.split(SEPARATOR).each {
                evictMultilanguageKey(it)
            }
            deleteKey(childrenKey)
        }
    }

    private void evictMultilanguageKey(String simpleKey) {
        String cachedLanguages = memcachedClient.get(simpleKey) as String
        if (cachedLanguages) {
            cachedLanguages.split(SEPARATOR).each {
                deleteKey(simpleKey + it)
            }
            deleteKey(simpleKey)
        }
    }

    private void deleteKey(String simpleKey) {
        memcachedClient.delete(simpleKey)
        logger.debug("Key ${simpleKey} deleted cache")
    }

    private writeTo(ServletResponse response, byte[] content) {
        OutputStream out = new BufferedOutputStream(response.getOutputStream())
        response.setContentLength(content.length)
        out.write(content)
        out.flush()
    }
}