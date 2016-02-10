package kuorum.causes

import groovyx.net.http.RESTClient
import kuorum.users.KuorumUser
import org.kuorum.rest.model.notification.KuorumMailAccountDetailsRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
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

    CauseRSDTO supportCause(KuorumUser user){

    }


    private String buildUrl(String path, Map<String,String> params){
        params.each{ k, v -> path = path.replaceAll("\\{${k}}", v) }
        apiPath+path
    }
}
