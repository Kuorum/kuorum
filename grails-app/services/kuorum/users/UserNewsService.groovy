package kuorum.users

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.kuorumUser.news.UserNewRSDTO

@Transactional
class UserNewsService {

    RestKuorumApiService restKuorumApiService;

    List<UserNewRSDTO> findUserNews(KuorumUser user) {
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_NEWS,
                [userId:user.id.toString()],
                [:],
                new TypeReference<List<UserNewRSDTO>>(){}
        )
        List<UserNewRSDTO>  leaningIndexRSDTO =  response.data

        return leaningIndexRSDTO;
    }
}
