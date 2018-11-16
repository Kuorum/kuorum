package kuorum.causes

import com.fasterxml.jackson.core.type.TypeReference
import kuorum.core.model.search.Pagination
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
import org.kuorum.rest.model.tag.SupportedCauseRSDTO
import org.kuorum.rest.model.tag.UsersSupportingCauseRSDTO

class CausesService {


    RestKuorumApiService restKuorumApiService

    IndexSolrService indexSolrService

    List<CauseRSDTO> findDefendedCauses(KuorumUser user) {
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CAUSES_DEFENDED,
                [userId:user.id.toString()],
                [:],
                new TypeReference<List<CauseRSDTO>>(){})
        List<CauseRSDTO> account = []
        if (response.data){
            account = (List<CauseRSDTO>)response.data
        }
        return account
    }

    @Deprecated
    List<CauseRSDTO> findSupportedCauses(KuorumUser user) {
        findSupportedCauses(user.id.toString())
    }
    List<CauseRSDTO> findSupportedCauses(KuorumUserSession user) {
        findSupportedCauses(user.id.toString())
    }
    List<CauseRSDTO> findSupportedCauses(String userId) {
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CAUSES_SUPPORTED,
                [userId:userId ],
                [:],
                new TypeReference<List<CauseRSDTO>>(){})
        List<CauseRSDTO> account = []
        if (response.data){
            account = (List<CauseRSDTO>)response.data
        }
        return account
    }

    CauseRSDTO createCause(String causeName){
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.CAUSE_OPERATIONS,
                [causeName:causeName],
                [:],
                null,
                new TypeReference<CauseRSDTO>(){})
        CauseRSDTO cause = null
        if (response.data){
            cause = response.data
        }
        return cause
    }

    SupportedCauseRSDTO supportCause(KuorumUserSession user, String causeName){
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CAUSES_SUPPORT,
                [userId:user.id.toString(), causeName:causeName],
                [:],
                null,
                new TypeReference<SupportedCauseRSDTO>(){})
        SupportedCauseRSDTO cause = null
        if (response.data){
            cause = response.data
        }
        return cause
    }

    SupportedCauseRSDTO unsupportCause(KuorumUserSession user, String causeName){
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CAUSES_SUPPORT,
                [userId:user.id.toString(), causeName:causeName],
                [:])
        SupportedCauseRSDTO cause = null
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause
    }

    @Deprecated
    SupportedCauseRSDTO defendCause(KuorumUser user, String causeName){
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CAUSES_DEFEND,
                [userId:user.id.toString(), causeName:causeName],
                [:],
                null,
                new TypeReference<SupportedCauseRSDTO>(){})
        SupportedCauseRSDTO cause = null
        if (response.data){
            cause = response.data
        }
        return cause
    }

    @Deprecated
    SupportedCauseRSDTO withdrawCause(KuorumUser user, String causeName){
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CAUSES_DEFEND,
                [userId:user.id.toString(), causeName:causeName],
                [:])
        SupportedCauseRSDTO cause = null
        if (response.data){
            cause = new SupportedCauseRSDTO(response.data)
        }
        return cause
    }

    SupportedCauseRSDTO statusCause(KuorumUserSession user, String causeName){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CAUSES_SUPPORT,
                [userId:user.id.toString(), causeName:causeName],
                [:],
                new TypeReference<SupportedCauseRSDTO>(){})
        SupportedCauseRSDTO cause = null
        if (response.data){
            cause = response.data
        }
        return cause
    }


    SuggestedCausesRSDTO suggestCauses(KuorumUser user, Pagination pagination = new Pagination()){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CAUSE_SUGGESTIONS,
                [:],
                [userId:user.id.toString(),page:Math.round(pagination.offset/pagination.max), size:pagination.max],
                new TypeReference<SuggestedCausesRSDTO>(){})
        SuggestedCausesRSDTO suggestions = null
        if (response.data){
            suggestions = response.data
        }
        return suggestions
    }

    void discardSuggestedCause(KuorumUser user, String causeName){
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.CAUSE_SUGGESTIONS,
                [:],
                [userId:user.id.toString(), causeName:causeName])
    }

    SupportedCauseRSDTO toggleSupportCause(KuorumUserSession user, String causeName){
        SupportedCauseRSDTO cause = statusCause(user, causeName)
        if (cause.supported){
            cause = unsupportCause(user,causeName)
        }else{
            cause = supportCause(user, causeName)
        }
        return cause
    }

    UsersSupportingCauseRSDTO mostRelevantSupporters(String causeName, Pagination page = new Pagination()){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CAUSE_USERS_SUPPORTING,
                [causeName:causeName],
                [page:page.offset/page.max, size: page.max],
                new TypeReference<UsersSupportingCauseRSDTO>(){})
        UsersSupportingCauseRSDTO supportingCauseRSDTO = null
        if (response.data){
            supportingCauseRSDTO = response.data
        }
        return supportingCauseRSDTO
    }
}
