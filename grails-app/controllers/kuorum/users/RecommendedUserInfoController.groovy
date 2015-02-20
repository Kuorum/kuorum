package kuorum.users

class RecommendedUserInfoController {
    def springSecurityService
    RecommendedUserInfoService recommendedUserInfoService

    def deleteRecommendedUser(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        KuorumUser deletedUser = KuorumUser.findById(params.deletedUserId)

        Map messageDeletingUser = recommendedUserInfoService.addUserToDelete(user, deletedUser)

        if (messageDeletingUser.error){
            flash.error = message(code:messageDeletingUser.message)
        }else{
            flash.message = message(code:messageDeletingUser.message)
        }
    }
}
