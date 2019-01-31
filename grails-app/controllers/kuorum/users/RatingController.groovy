package kuorum.users

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO

class RatingController {

    SpringSecurityService springSecurityService
    KuorumUserService kuorumUserService
    UserReputationService userReputationService
    PoliticianService politicianService

    def ratePolitician(String userAlias){
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(userAlias)
        Integer rate = Integer.parseInt(params.rate)
        UserReputationRSDTO userReputationRSDTO = userReputationService.addReputation(user,rate)
        render userReputationRSDTO as JSON
    }

    def loadRating(String userAlias){
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(userAlias)
        UserReputationRSDTO userReputationRSDTO = userReputationService.getReputationWithCache(user)
        render template: "/kuorumUser/popoverUserRating", model: [user:user,userReputation: userReputationRSDTO]
    }

}
