package kuorum.users

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.news.UserNewRSDTO

@Transactional
class UserNewsService {

    RestKuorumApiService restKuorumApiService

    List<UserNewRSDTO> findUserNews(KuorumUserSession loggedUser) {
        return findUserNews(loggedUser.id.toString())
    }
    List<UserNewRSDTO> findUserNews(BasicDataKuorumUserRSDTO user) {
        return findUserNews(user.id)
    }
    List<UserNewRSDTO> findUserNews(String userId) {
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_NEWS,
                [userId:userId],
                [:],
                new TypeReference<List<UserNewRSDTO>>(){}
        )
        List<UserNewRSDTO>  userNews =  response.data

        return userNews
    }
}
