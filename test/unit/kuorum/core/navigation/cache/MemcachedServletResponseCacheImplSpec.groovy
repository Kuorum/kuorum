package kuorum.core.navigation.cache

import grails.plugin.springsecurity.SpringSecurityService
import net.spy.memcached.MemcachedClient
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.ServletResponse

class MemcachedServletResponseCacheImplSpec extends Specification {

    public static final String URL_TEST_1 = "http://page1.com"
    public static final String URL_TEST_2 = "http://page2.com"

    SpringSecurityService securityService = Stub()
    MemcachedClient memcachedClient = Mock()

    @Subject
    MemcachedServletResponseCacheImpl memcachedServletResponseCache = new MemcachedServletResponseCacheImpl(memcachedClient, securityService)

    ResponseContent responseContent = Stub() {
        getContent() >> "Content".getBytes()
    }

    CacheHttpServletResponseWrapper wrapper = Stub() {
        getContent() >> responseContent
    }
    ServletResponse response = Stub()

    URL url1 = new URL(URL_TEST_1)

    void 'when call to evict all languages cached must be invalidated'() {
        given:
        memcachedClient.get(URL_TEST_1) >> 'en&es&ca'
        when:
        memcachedServletResponseCache.evict(url1)
        then:
        1 * memcachedClient.delete(URL_TEST_1 + 'en')
        1 * memcachedClient.delete(URL_TEST_1 + 'es')
        1 * memcachedClient.delete(URL_TEST_1 + 'ca')
        0 * memcachedClient.delete(URL_TEST_2 + 'es')
        0 * memcachedClient.delete(URL_TEST_2 + 'en')
        0 * memcachedClient.delete(URL_TEST_2 + 'ca')
    }
}
