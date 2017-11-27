package kuorum.users

import com.fasterxml.jackson.core.type.TypeReference
import grails.plugin.cookie.CookieService
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import kuorum.util.rest.RestKuorumApiService
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable

@Transactional
class UserReputationService {

    RestKuorumApiService restKuorumApiService;
    SpringSecurityService springSecurityService
    CookieUUIDService cookieUUIDService

    @CacheEvict(value='reputation', key='#politician.id')
    UserReputationRSDTO addReputation(KuorumUser politician, Integer evaluation) {

        String evaluatorId = cookieUUIDService.getUserUUID();
        Map<String, String> params = [userId:politician.id.toString()]
        Map<String, String> query = [evaluation:evaluation]
        if (evaluatorId){
            query.put("evaluatorId", evaluatorId)
        }
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION,
                params,
                query,
                null,
                new TypeReference<UserReputationRSDTO>(){})
        UserReputationRSDTO userReputation = null
        if (response.data){
            userReputation = (UserReputationRSDTO)response.data
        }
        cookieUUIDService.setUserUUID(userReputation.evaluatorId)
        return userReputation;
    }

    @Cacheable(value='reputation', key = '#politician.id')
    UserReputationRSDTO getReputationWithCache(KuorumUser politician) {
        return getReputation(politician)
    }
    UserReputationRSDTO getReputation(KuorumUser politician) {
        String evaluatorId = cookieUUIDService.getUserUUID();
        Map<String, String> params = [userId:politician.id.toString()]
        Map<String, String> query = [evaluatorId:evaluatorId]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION,
                params,
                query,
                new TypeReference<UserReputationRSDTO>(){})
        UserReputationRSDTO userReputation = null
        if (response.data){
            userReputation = (UserReputationRSDTO)response.data
        }
        return userReputation;
    }

    UserReputationEvolutionRSDTO getReputationEvoulution(
            KuorumUser politician,
            UserReputationEvolutionRSDTO.Interval interval = UserReputationEvolutionRSDTO.Interval.HOUR,
            Date startDate = null,
            Date endDate = null){
        Map<String, String> params = [userId:politician.id.toString()]
        Map<String, String> query = [interval:interval.toString()]
        if (startDate) query.put("startDate", startDate.time)
        if (endDate) query.put("endDate", endDate.time)
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_STATS_REPUTATION_EVOLUTION,
                params,
                query,
                new TypeReference<UserReputationEvolutionRSDTO>(){})
        UserReputationEvolutionRSDTO userReputationEvolution = null
        if (response.data){
            userReputationEvolution = (UserReputationEvolutionRSDTO)response.data
        }
        return userReputationEvolution;
    }
}
