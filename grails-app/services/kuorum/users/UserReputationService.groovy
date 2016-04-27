package kuorum.users

import grails.transaction.Transactional
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO

@Transactional
class UserReputationService {

    RestKuorumApiService restKuorumApiService;

    UserReputationRSDTO addReputation(KuorumUser politician, String evaluatorId = null, Integer evaluation) {

        Map<String, String> params = [userId:politician.id.toString()]
        Map<String, String> query = [evaluation:evaluation]
        if (evaluatorId){
            query.put("evaluatorId", evaluatorId)
        }
        def response = restKuorumApiService.post(RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION, params, query, null)
        UserReputationRSDTO userReputation = null
        if (response.data){
            userReputation = (UserReputationRSDTO)response.data
        }
        return userReputation;
    }

    UserReputationRSDTO getReputation(KuorumUser politician, String evaluatorId) {
        Map<String, String> params = [userId:politician.id.toString()]
        Map<String, String> query = [evaluatorId:evaluatorId]
        def response = restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION, params, query)
        UserReputationRSDTO userReputation = null
        if (response.data){
            userReputation = (UserReputationRSDTO)response.data
        }
        return userReputation;
    }

    UserReputationEvolutionRSDTO getReputationEvoulution(KuorumUser politician){
        Map<String, String> params = [userId:politician.id.toString()]
        def response = restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION_EVOLUTION, params, null)
        UserReputationEvolutionRSDTO userReputationEvolution = null
        if (response.data){
            userReputationEvolution = (UserReputationEvolutionRSDTO)response.data
        }
        return userReputationEvolution;
    }
}
