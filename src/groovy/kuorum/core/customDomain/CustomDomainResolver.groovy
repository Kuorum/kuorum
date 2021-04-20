package kuorum.core.customDomain

import grails.util.Holders
import org.apache.commons.collections.map.HashedMap
import org.kuorum.rest.client.KuorumApi
import org.kuorum.rest.model.domain.DomainRSDTO

class CustomDomainResolver {
    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>()

    private static final String PARAM_URL = "URL"
    private static final String PARAM_BASE = "URL_BASE"
    private static final String PARAM_DOMAIN = "URL_DOMAIN"
    private static final String PARAM_DOMAIN_CONFIG = "DOMAIN_CONFIG"

    //Not destroyed each call
    private static final Map<String, KuorumApi> API_TOKENS= new HashMap<String, KuorumApi>();

    static void setUrl(URL url, String contextPath="") {
//        System.out.print("ThreadLocal [START] ->"+Thread.currentThread().id)
        Map<String, Object> cachedData
        if (CONTEXT.get()){
            cachedData = CONTEXT.get()
        }else{
            cachedData = new HashedMap()

        }
        cachedData.put(PARAM_URL,URL)
        String domain = url.getHost()
        String port = url.getPort()==80 || url.getPort() <=0 ?'':":${url.getPort()}"
//        String protocol = url.getProtocol()
//        String base = "${protocol}://${domain}${port}${contextPath}"
        String base = "https://${domain}${port}${contextPath}" // AWS Sent http protocol instead https
        cachedData.put(PARAM_BASE,base)
        cachedData.put(PARAM_DOMAIN,domain)
        CONTEXT.set(cachedData)
        if (!API_TOKENS.get(domain)){
            String apiUrl = Holders.getGrailsApplication().config.kuorum.rest.url
            String oauthPath = Holders.getGrailsApplication().config.kuorum.rest.authPath
            String clientId = Holders.getGrailsApplication().config.kuorum.rest.client_id
            String clientSecret = Holders.getGrailsApplication().config.kuorum.rest.client_secret
            KuorumApi kuorumApi = new KuorumApi(apiUrl+oauthPath, clientId, domain,clientSecret)
            API_TOKENS.put(domain, kuorumApi)
        }
    }

    static String getDomain() {
//        System.out.print("ThreadLocal [GET] ->"+Thread.currentThread().id)
        return CONTEXT.get().get(PARAM_DOMAIN)
    }

    static void clear() {
//        System.out.print("ThreadLocal [CLEAR] ->"+Thread.currentThread().id)
        if (CONTEXT && CONTEXT.get()){
            CONTEXT.get().clear()
        }
        CONTEXT.remove()
    }

    static String getBaseUrlAbsolute(){
        if (CONTEXT.get()){
            return CONTEXT.get().get(PARAM_BASE)
        }else{
            return "https://kuorum.org"
        }
    }

    static String getApiToken(){
        String domainName = CONTEXT.get().get(PARAM_DOMAIN);
        return API_TOKENS.get(domainName).getAuthorizationHeader();
    }

    static String getAdminApiToken(){
        String domainName = "kuorum.org";
        return API_TOKENS.get(domainName).getAuthorizationHeader();
    }

    static DomainRSDTO getDomainRSDTO(){
        return CONTEXT.get().get(PARAM_DOMAIN_CONFIG)
    }

    static void setDomainRSDTO(DomainRSDTO domainConfig){
        CONTEXT.get().put(PARAM_DOMAIN_CONFIG, domainConfig)
    }
}
