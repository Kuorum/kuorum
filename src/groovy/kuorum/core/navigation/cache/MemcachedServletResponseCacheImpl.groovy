package kuorum.core.navigation.cache

import grails.plugin.springsecurity.SpringSecurityService
import net.spy.memcached.MemcachedClient
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import javax.servlet.ServletResponse

class MemcachedServletResponseCacheImpl extends AbstractUrlKeyCacheImpl {

    Integer expirationInADay = 24 * 60 * 60 * 1 // 1 days
    private final static String SEPARATOR = "&"

    protected final Log logger = LogFactory.getLog(getClass())

    MemcachedClient memcachedClient

    MemcachedServletResponseCacheImpl(){}

    MemcachedServletResponseCacheImpl(MemcachedClient memcachedClient, SpringSecurityService springSecurityService) {
        super(springSecurityService)
        this.memcachedClient = memcachedClient
    }

    @Override
    void put(URL url, CacheHttpServletResponseWrapper response, Locale locale) {
        def key = buildKey(url, locale)
        memcachedClient.set(key, expirationInADay, response.getContent().getContent())

        def appendTry = memcachedClient.append(buildKey(url), SEPARATOR + localeInfo(locale))
        if (appendTry.getStatus().isSuccess()) {
            logger.debug("language ${localeInfo(locale)} append")
        }else{
            memcachedClient.set(buildKey(url), expirationInADay, localeInfo(locale))
            logger.debug("language ${localeInfo(locale)} new cached")
        }
        logger.debug("Key $key cached")
    }

    @Override
    boolean get(URL url, ServletResponse response, Locale locale) {
        def key = buildKey(url, locale)
        byte[] cached = memcachedClient.get(key)
        if (cached == null) {
            return false
        }
        writeTo(response, cached)
        logger.debug("Key $key recovered from cache")
        return true
    }

    @Override
    void evict(URL url) {
        def simpleKey = buildKey(url)
        (memcachedClient.get(simpleKey) as String).split(SEPARATOR).each {
            memcachedClient.delete(simpleKey + it)
            logger.debug("Key ${simpleKey + it} deleted cache")
        }
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
