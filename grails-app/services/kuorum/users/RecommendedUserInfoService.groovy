package kuorum.users

import grails.transaction.Transactional

@Transactional
class RecommendedUserInfoService {

    Map addUserToDelete(KuorumUser user, KuorumUser deletedUser) {
        RecommendedUserInfo recommendedUserInfo = RecommendedUserInfo.findByUser(user)?: new RecommendedUserInfo(user:user).save(flush:true)

        if (recommendedUserInfo && deletedUser && recommendedUserInfo.validate()){
            recommendedUserInfo.deletedRecommendedUsers << deletedUser.id
            try {
                if (recommendedUserInfo.save()) {
                    [message: 'recommendedUserInfoService.addUserToDelete.savingDeleteUserSuccessfully', error:false]
                } else {
                    [message: 'recommendedUserInfoService.addUserToDelete.errorSavingDeleteUser', error:true]
                }
            } catch (e) {
                [message: 'recommendedUserInfoService.addUserToDelete.errorSavingDeleteUser', error:true]
                log.error "Error saving the object : ${e.message}"
            }
        }else{
            [message:'recommendedUserInfoService.addUserToDelete.errorValidatingDeleteUser', error:true]
        }
    }
}
