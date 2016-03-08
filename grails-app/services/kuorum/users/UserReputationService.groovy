package kuorum.users

import grails.transaction.Transactional
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO

@Transactional
class UserReputationService {

    RestKuorumApiService restKuorumApiService;

    UserReputationRSDTO addReputation(KuorumUser politician, KuorumUser citizen = null, Integer reputation) {

        Map<String, String> params = [userId:politician.id.toString()]
        Map<String, String> query = [reputation:reputation]
        if (citizen){
            query.put("evaluatorId", citizen.getId().toString())
        }
        def response = restKuorumApiService.put(RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION, params, query)
        UserReputationRSDTO userReputation = null
        if (response.data){
            userReputation = (UserReputationRSDTO)response.data
        }
        return userReputation;
    }

    UserReputationRSDTO getReputation(KuorumUser politician, KuorumUser citizen = null) {
        Map<String, String> params = [userId:politician.id.toString()]
        Map<String, String> query = [:]
        if (citizen){
            query.put("evaluatorId", citizen.getId().toString())
        }
        def response = restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION, params, query)
        UserReputationRSDTO userReputation = null
        if (response.data){
            userReputation = (UserReputationRSDTO)response.data
        }
        return userReputation;
    }
}
