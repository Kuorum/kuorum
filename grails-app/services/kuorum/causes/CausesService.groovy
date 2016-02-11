package kuorum.causes

import groovyx.net.http.RESTClient
import kuorum.users.KuorumUser
import org.kuorum.rest.model.notification.KuorumMailAccountDetailsRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SupportedCauseRSDTO
import org.springframework.beans.factory.annotation.Value

class CausesService {

    @Value('${kuorum.rest.url}')
    String kuorumRestServices

    @Value('${kuorum.rest.apiPath}')
    String apiPath

    @Value('${kuorum.rest.apiKey}')
    String kuorumRestApiKey

    String CAUSE_INFO="/cause/{causeName}"
    String USER_CAUSES="/cause/support/{userId}"
    String USER_CAUSE_OPERATIONS="/cause/support/{causeName}/{userId}"

    List<CauseRSDTO> findUserCauses(KuorumUser user) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(USER_CAUSES, [userId:user.id.toString()]);
        def response = mailKuorumServices.get(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:[:],
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        List<CauseRSDTO> account = [];
        if (response.data){
            account = (List<CauseRSDTO>)response.data
        }
        return account;
    }

    SupportedCauseRSDTO supportCause(KuorumUser user, String causeName){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(USER_CAUSE_OPERATIONS, [userId:user.id.toString(), causeName:causeName]);
        def response = mailKuorumServices.post(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:[:],
                body:[],
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        SupportedCauseRSDTO cause = null;
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause;
    }

    SupportedCauseRSDTO unsupportCause(KuorumUser user, String causeName){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(USER_CAUSE_OPERATIONS, [userId:user.id.toString(), causeName:causeName]);
        def response = mailKuorumServices.delete(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:[:],
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        SupportedCauseRSDTO cause = null;
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause;
    }

    SupportedCauseRSDTO statusCause(KuorumUser user, String causeName){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(USER_CAUSE_OPERATIONS, [userId:user.id.toString(), causeName:causeName]);
        def response = mailKuorumServices.get(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:[:],
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        SupportedCauseRSDTO cause = null;
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause;
    }


    SupportedCauseRSDTO toggleSupportCause(KuorumUser user, String causeName){
        SupportedCauseRSDTO cause = statusCause(user, causeName)
        if (cause.supported){
            cause = unsupportCause(user,causeName)
        }else{
            cause = supportCause(user, causeName)
        }
        return cause
    }
    private String buildUrl(String path, Map<String,String> params){
        params.each{ k, v -> path = path.replaceAll("\\{${k}}", v) }
        apiPath+path
    }
}
