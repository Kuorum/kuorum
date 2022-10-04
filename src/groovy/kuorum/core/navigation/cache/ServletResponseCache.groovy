package kuorum.core.navigation.cache


import javax.servlet.ServletResponse

interface ServletResponseCache {

    void put(URL url, CacheHttpServletResponseWrapper response, Locale locale)

    boolean get(URL url, ServletResponse response, Locale locale)

    void evict(URL url)

}