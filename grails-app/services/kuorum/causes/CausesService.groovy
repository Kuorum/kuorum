package kuorum.causes

import kuorum.core.model.UserType
import kuorum.core.model.search.Pagination
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
import org.kuorum.rest.model.tag.SupportedCauseRSDTO
import org.kuorum.rest.model.tag.UsersSupportingCauseRSDTO

class CausesService {


    RestKuorumApiService restKuorumApiService;

    IndexSolrService indexSolrService;

    List<CauseRSDTO> findDefendedCauses(KuorumUser user) {
        def response = restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_CAUSES_DEFENDED, [userId:user.id.toString()],[:])
        List<CauseRSDTO> account = [];
        if (response.data){
            account = (List<CauseRSDTO>)response.data
        }
        return account;
    }

    List<CauseRSDTO> findSupportedCauses(KuorumUser user) {
        def response = restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_CAUSES_SUPPORTED, [userId:user.id.toString()],[:])
        List<CauseRSDTO> account = [];
        if (response.data){
            account = (List<CauseRSDTO>)response.data
        }
        return account;
    }

    CauseRSDTO createCause(String causeName){
        def response = restKuorumApiService.put(RestKuorumApiService.ApiMethod.CAUSE_OPERATIONS, [causeName:causeName],[:])
        CauseRSDTO cause = null;
        if (response.data){
            cause = new CauseRSDTO(response.data)
        }
        return cause;
    }

    SupportedCauseRSDTO supportCause(KuorumUser user, String causeName){
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CAUSES_SUPPORT,
                [userId:user.id.toString(), causeName:causeName],
                [:])
        SupportedCauseRSDTO cause = null;
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause;
    }

    SupportedCauseRSDTO unsupportCause(KuorumUser user, String causeName){
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CAUSES_SUPPORT,
                [userId:user.id.toString(), causeName:causeName],
                [:])
        SupportedCauseRSDTO cause = null;
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause;
    }

    SupportedCauseRSDTO defendCause(KuorumUser user, String causeName){
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CAUSES_DEFEND,
                [userId:user.id.toString(), causeName:causeName],
                [:])
        SupportedCauseRSDTO cause = null;
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause;
    }

    SupportedCauseRSDTO withdrawCause(KuorumUser user, String causeName){
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CAUSES_DEFEND,
                [userId:user.id.toString(), causeName:causeName],
                [:])
        SupportedCauseRSDTO cause = null;
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause;
    }

    SupportedCauseRSDTO statusCause(KuorumUser user, String causeName){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CAUSES_SUPPORT,
                [userId:user.id.toString(), causeName:causeName],
                [:])
        SupportedCauseRSDTO cause = null;
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause;
    }


    SuggestedCausesRSDTO suggestCauses(KuorumUser user, Pagination pagination = new Pagination()){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CAUSE_SUGGESTIONS,
                [:],
                [userId:user.id.toString(),page:Math.round(pagination.offset/pagination.max), size:pagination.max])
        SuggestedCausesRSDTO suggestions = null;
        if (response.data){
            suggestions = new SuggestedCausesRSDTO(response.data)
        }
        return suggestions;
    }

    void discardSuggestedCause(KuorumUser user, String causeName){
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.CAUSE_SUGGESTIONS,
                [:],
                [userId:user.id.toString(), causeName:causeName])
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

    UsersSupportingCauseRSDTO mostRelevantDefenders(String causeName){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CAUSE_USERS_DEFENDING,
                [causeName:causeName],
                [:])
        UsersSupportingCauseRSDTO supportingCauseRSDTO = null;
        if (response.data){
            supportingCauseRSDTO = new UsersSupportingCauseRSDTO(response.data)
        }
        return supportingCauseRSDTO;
    }
    UsersSupportingCauseRSDTO mostRelevantSupporters(String causeName){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CAUSE_USERS_SUPPORTING,
                [causeName:causeName],
                [:])
        UsersSupportingCauseRSDTO supportingCauseRSDTO = null;
        if (response.data){
            supportingCauseRSDTO = new UsersSupportingCauseRSDTO(response.data)
        }
        return supportingCauseRSDTO;
    }
}
