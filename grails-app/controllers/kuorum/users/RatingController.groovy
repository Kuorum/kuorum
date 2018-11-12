package kuorum.users

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO

class RatingController {

    SpringSecurityService springSecurityService
    KuorumUserService kuorumUserService
    UserReputationService userReputationService
    PoliticianService politicianService

    def ratePolitician(String userAlias){
        KuorumUser politician = kuorumUserService.findByAlias(userAlias)
        Integer rate = Integer.parseInt(params.rate)
        UserReputationRSDTO userReputationRSDTO = userReputationService.addReputation(politician,rate)
        render userReputationRSDTO as JSON
    }

    @Deprecated
    def widgetRatePolitician = {
        List<KuorumUser> politicians = params.userAlias.collect{kuorumUserService.findByAlias(it)}.findAll{it!= null}
        Map<String, UserReputationRSDTO> rates = [:]
        politicians.each{politician ->
            UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(politician)
            rates.put(politician.alias, userReputationRSDTO)
        }
        [politicians :politicians, rates:rates ]
    }


    def loadRating(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        UserReputationRSDTO userReputationRSDTO = userReputationService.getReputationWithCache(user)
        render template: "/kuorumUser/popoverUserRating", model: [user:user,userReputation: userReputationRSDTO]
    }

}
