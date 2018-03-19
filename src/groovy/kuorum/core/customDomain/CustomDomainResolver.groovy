package kuorum.core.customDomain

import org.apache.commons.collections.map.HashedMap

class CustomDomainResolver {
    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    private static final String PARAM_URL = "URL";
    private static final String PARAM_BASE = "URL_BASE";
    private static final String PARAM_DOMAIN = "URL_DOMAIN";
    private static final String PARAM_DOMAIN_TOKEN = "DOMAIN_TOKEN";

    public static void setUrl(URL url, String contextPath="") {
        System.out.print("ThreadLocal [START] ->"+Thread.currentThread().id)
        Map<String, Object> cachedData
        if (CONTEXT.get()){
            cachedData = CONTEXT.get()
        }else{
            cachedData = new HashedMap()

        }
        cachedData.put(PARAM_URL,URL)
        String domain = url.getHost();
        String port = url.getPort()==80 || url.getPort() <=0 ?'':":${url.getPort()}"
        String protocol = url.getProtocol()
        String base = "${protocol}://${domain}${port}${contextPath}"
        cachedData.put(PARAM_BASE,base)
        cachedData.put(PARAM_DOMAIN,domain)
        CONTEXT.set(cachedData);
    }

    public static String getDomain() {
//        System.out.print("ThreadLocal [GET] ->"+Thread.currentThread().id)
        return CONTEXT.get().get(PARAM_DOMAIN);
    }

    public static void clear() {
        System.out.print("ThreadLocal [CLEAR] ->"+Thread.currentThread().id)
//        CONTEXT.remove();
    }

    public static String getBaseUrlAbsolute(){
//        System.out.print("ThreadLocal [GET] ->"+Thread.currentThread().id)
        return CONTEXT.get().get(PARAM_BASE)
    }

    public static String setApiToken(String token){
        return CONTEXT.get().put(PARAM_DOMAIN_TOKEN, token)
    }

    public static String getApiToken(){
        return CONTEXT.get().get(PARAM_DOMAIN_TOKEN)
    }
}
