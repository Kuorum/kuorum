package kuorum.core.navigation.cache

import grails.plugin.springsecurity.SpringSecurityService
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import javax.servlet.ServletResponse

class HasMapServletResponseCacheImpl extends AbstractUrlKeyCacheImpl {

    HasMapServletResponseCacheImpl(SpringSecurityService springSecurityService) {
        super(springSecurityService)
    }

    protected final Log logger = LogFactory.getLog(getClass());

    HashMap<String, ResponseContent> cache = new HashMap<>()

    @Override
    void put(URL url, CacheHttpServletResponseWrapper wrapper, Locale locale) {

        def key = buildKey(url, locale)
        cache.put(key, wrapper.getContent())
        logger.info("Key $key cached")
    }

    @Override
    boolean get(URL url, ServletResponse response, Locale locale) {
        String key = buildKey(url, locale)
        if (cache.containsKey(key)) {
            logger.info("Key $key recover from cache")
            cache.get(key).writeTo(response)
            return true
        }
        return false
    }

    @Override
    void evict(URL url) {
        String keyStart = buildKey(url)
        cache.keySet().findAll {key -> key.startsWith(keyStart) }
                .each {keyFound -> cache.remove(keyFound) }
    }
}
