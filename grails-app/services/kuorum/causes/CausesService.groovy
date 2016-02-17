package kuorum.causes

import groovyx.net.http.RESTClient
import kuorum.core.model.search.Pagination
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
import org.kuorum.rest.model.tag.SupportedCauseRSDTO
import org.kuorum.rest.model.tag.UsersSupportingCauseRSDTO
import org.springframework.beans.factory.annotation.Value

class CausesService {

    @Value('${kuorum.rest.url}')
    String kuorumRestServices

    @Value('${kuorum.rest.apiPath}')
    String apiPath

    @Value('${kuorum.rest.apiKey}')
    String kuorumRestApiKey

    IndexSolrService indexSolrService;

    private String CAUSE_OPERATIONS="/cause/{causeName}"
    private String CAUSE_SUPPORT ="/cause/support/{userId}"
    private String CAUSE_SUPPORT_OPERATIONS ="/cause/support/{causeName}/{userId}"
    private String CAUSE_SUGGESTIONS ="/cause/suggest"
    private String CAUSE_SUGGESTIONS_USER ="/cause/suggest/{userId}"
    private String CAUSE_POLITICIANS ="/cause/{causeName}/politicians"

    List<CauseRSDTO> findUserCauses(KuorumUser user) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(CAUSE_SUPPORT, [userId:user.id.toString()]);
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

    CauseRSDTO createCause(String causeName){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(CAUSE_OPERATIONS, [ causeName:causeName]);
        def response = mailKuorumServices.put(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:[:],
                body:[],
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        CauseRSDTO cause = null;
        if (response.data){
            cause = new CauseRSDTO(response.data)
        }
        return cause;
    }

    SupportedCauseRSDTO supportCause(KuorumUser user, String causeName){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(CAUSE_SUPPORT_OPERATIONS, [userId:user.id.toString(), causeName:causeName]);
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
        String path = buildUrl(CAUSE_SUPPORT_OPERATIONS, [userId:user.id.toString(), causeName:causeName]);
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
        String path = buildUrl(CAUSE_SUPPORT_OPERATIONS, [userId:user.id.toString(), causeName:causeName]);
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


    SuggestedCausesRSDTO suggestCauses(KuorumUser user, Pagination pagination = new Pagination()){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(CAUSE_SUGGESTIONS_USER, [userId:user.id.toString()]);
        def response = mailKuorumServices.get(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:[page:Math.round(pagination.offset/pagination.max), size:pagination.max],
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        SuggestedCausesRSDTO suggestions = null;
        if (response.data){
            suggestions = new SuggestedCausesRSDTO(response.data)
        }
        return suggestions;
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

    UsersSupportingCauseRSDTO mostRelevantPoliticianForCause(String causeName){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(CAUSE_POLITICIANS, [causeName:causeName]);
        def response = mailKuorumServices.get(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:[:],
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        UsersSupportingCauseRSDTO supportingCauseRSDTO = null;
        if (response.data){
            supportingCauseRSDTO = new UsersSupportingCauseRSDTO(response.data)
        }
        return supportingCauseRSDTO;
    }


    private String buildUrl(String path, Map<String,String> params){
        params.each{ k, v -> path = path.replaceAll("\\{${k}}", v) }
        apiPath+path
    }
}
