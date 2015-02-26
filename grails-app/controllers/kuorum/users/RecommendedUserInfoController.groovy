package kuorum.users

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class RecommendedUserInfoController {
    def springSecurityService
    RecommendedUserInfoService recommendedUserInfoService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def deleteRecommendedUser(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        KuorumUser deletedUser = KuorumUser.findById(params.deletedUserId)

        Map messageDeletingUser = recommendedUserInfoService.addUserToDelete(user, deletedUser)
        Map result = [error:messageDeletingUser.error, message: messageDeletingUser.message?message(code:messageDeletingUser.message):'']
        render result as JSON
    }
}
