package kuorum.core.navigation.cache

import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo

import javax.servlet.ServletResponse

interface ServletRequestResponseCache {

    void put(UrlMappingInfo urlMappingInfo, CacheHttpServletResponseWrapper response, Locale locale)

    boolean get(UrlMappingInfo urlMappingInfo, ServletResponse response, Locale locale)

    void evictCampaign(UrlMappingInfo urlMappingInfo)

    void evictGlobal()

}