package kuorum.core.navigation.cache


import javax.servlet.ServletResponse

interface ServletResponseCache {

    void put(URL url, ResponseContent response)

    boolean get(URL url, ServletResponse response)

    void evict(URL url)

}