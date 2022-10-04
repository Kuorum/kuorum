package kuorum.core.navigation.cache

import grails.plugin.springsecurity.SpringSecurityService
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.ServletResponse

class HasMapServletResponseCacheImplSpec extends Specification {

    public static final String URL_TEST_1 = "http://page1.com"
    public static final String URL_TEST_2 = "http://page2.com"

    SpringSecurityService securityService = Stub()

    @Subject
    HasMapServletResponseCacheImpl hasMapServletResponseCache = new HasMapServletResponseCacheImpl(securityService)

    ResponseContent responseContent = Stub()

    CacheHttpServletResponseWrapper wrapper = Stub(){
        getContent() >> responseContent
    }
    ServletResponse response = Stub()

    Locale locale1 = Locale.ENGLISH

    Locale locale2 = Locale.FRENCH

    URL url1 = new URL(URL_TEST_1)
    URL url2 = new URL(URL_TEST_2)

    void 'When call to evict all languages cached must be invalidated'() {
        given:
        hasMapServletResponseCache.put(url1, wrapper, locale1)
        hasMapServletResponseCache.put(url2, wrapper, locale1)
        hasMapServletResponseCache.put(url1, wrapper, locale2)
        when:
        hasMapServletResponseCache.evict(url1)
        then:
        hasMapServletResponseCache.get(url1, response, locale1) == false
        hasMapServletResponseCache.get(url1, response, locale2) == false
        hasMapServletResponseCache.get(url2, response, locale1) == true
    }
}
