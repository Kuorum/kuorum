package kuorum.core.navigation.cache

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.customDomain.filter.ResponseContent
import kuorum.register.KuorumUserSession

import javax.servlet.ServletResponse

class HasMapServletResponseCacheImpl implements ServletResponseCache {

    SpringSecurityService springSecurityService

    HashMap<String, ResponseContent> cache = new HashMap<>()

    String buildKey(URL url) {
        if (springSecurityService.isLoggedIn()) {
            return url.toString() + ((KuorumUserSession) springSecurityService.principal).id
        } else {
            return url.toString();
        }
    }

    @Override
    void put(URL url, ResponseContent response) {
        cache.put(buildKey(url), response)
    }

    @Override
    boolean get(URL url, ServletResponse response) {
        String key = buildKey(url)
        if(cache.containsKey(key)){
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
