package kuorum.users

import grails.plugin.cookie.CookieService
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.kuorumUser.news.UserNewRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO

@Transactional
class UserNewsService {

    RestKuorumApiService restKuorumApiService;

    List<UserNewRSDTO> findUserNews(KuorumUser user) {
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_NEWS,
                [userId:user.id.toString()],
                [:]
        )
        List<UserNewRSDTO>  leaningIndexRSDTO =  (List<UserNewRSDTO> )response.data

        return leaningIndexRSDTO;
    }
}
