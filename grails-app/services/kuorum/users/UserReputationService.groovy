package kuorum.users

import grails.plugin.cookie.CookieService
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO

@Transactional
class UserReputationService {

    RestKuorumApiService restKuorumApiService;
    SpringSecurityService springSecurityService
    CookieService cookieService

    UserReputationRSDTO addReputation(KuorumUser politician, Integer evaluation) {

        String evaluatorId =getEvaluatorUserId()
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
        setEvaluatorUserId(userReputation.evaluatorId)
        return userReputation;
    }

    private static final String COOKIE_EVALUATOR_NAME='EVALUATOR_ID_RATING'

    private String getEvaluatorUserId(){
        String evaluatorId = cookieService.getCookie(COOKIE_EVALUATOR_NAME)
        if (springSecurityService.isLoggedIn()){
            evaluatorId = springSecurityService.currentUser.id.toString()
        }
        return evaluatorId;
    }

    private void setEvaluatorUserId(String evaluatorId){
        cookieService.setCookie(
                [name:COOKIE_EVALUATOR_NAME,
                 value:evaluatorId,
                 maxAge:Integer.MAX_VALUE ,
                 path:"/",
                 domain:null])
    }

    UserReputationRSDTO getReputation(KuorumUser politician) {
        String evaluatorId = getEvaluatorUserId();
        Map<String, String> params = [userId:politician.id.toString()]
        Map<String, String> query = [evaluatorId:evaluatorId]
        def response = restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION, params, query)
        UserReputationRSDTO userReputation = null
        if (response.data){
            userReputation = (UserReputationRSDTO)response.data
        }
        return userReputation;
    }

    UserReputationEvolutionRSDTO getReputationEvoulution(KuorumUser politician, UserReputationEvolutionRSDTO.Interval interval = UserReputationEvolutionRSDTO.Interval.HOUR){
        Map<String, String> params = [userId:politician.id.toString()]
        Map<String, String> query = [interval:interval.toString()]
        def response = restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION_EVOLUTION, params, query)
        UserReputationEvolutionRSDTO userReputationEvolution = null
        if (response.data){
            userReputationEvolution = (UserReputationEvolutionRSDTO)response.data
        }
        return userReputationEvolution;
    }
}
