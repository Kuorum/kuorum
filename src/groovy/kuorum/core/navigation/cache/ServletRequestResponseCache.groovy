package kuorum.core.navigation.cache


import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

interface ServletRequestResponseCache {

    void put(HttpServletRequest request, CacheHttpServletResponseWrapper response)

    boolean get(HttpServletRequest request, ServletResponse response)

    void evictCampaing(HttpServletRequest request)

    void evictGlobal(HttpServletRequest request)

}