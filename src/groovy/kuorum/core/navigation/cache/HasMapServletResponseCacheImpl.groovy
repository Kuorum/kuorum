package kuorum.core.navigation.cache


import javax.servlet.ServletResponse

class HasMapServletResponseCacheImpl extends AbstractUrlKeyCacheImpl implements ServletResponseCache {

    HashMap<String, ResponseContent> cache = new HashMap<>()

    @Override
    void put(URL url, ResponseContent response) {
        cache.put(buildKey(url), response)
    }

    @Override
    boolean get(URL url, ServletResponse response) {
        String key = buildKey(url)
        if (cache.containsKey(key)) {
            cache.get(key).writeTo(response)
            return true
        }
        return false
    }

    @Override
    void evict(URL url) {
        cache.remove(buildKey(url))
    }
}
