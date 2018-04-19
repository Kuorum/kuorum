package kuorum.users

import kuorum.helper.IntegrationHelper
import spock.lang.Shared
import spock.lang.Specification


class RecommendedUserInfoServiceIntegrationSpec extends Specification{

    @Shared
    RecommendedUserInfoService recommendedUserInfoService

    def "test to check if a validate user can delete recommended user"(){
        given:
        KuorumUser userInSession = (IntegrationHelper.createDefaultUser("userInSession@example.es")).save(flush:true)
        KuorumUser userToDelete = (IntegrationHelper.createDefaultUser("userToDelete@example.es")).save(flush:true)

        RecommendedUserInfo recommendedUserInfo = (new RecommendedUserInfo(user:userInSession, deletedRecommendedUsers:[])).save(flush:true)

        when:
        Map result = recommendedUserInfoService.addUserToDelete(userInSession,userToDelete)

        then:
        recommendedUserInfo.refresh()
        recommendedUserInfo.deletedRecommendedUsers
        recommendedUserInfo.deletedRecommendedUsers.size() == 1
        recommendedUserInfo.deletedRecommendedUsers.contains(userToDelete.id)

        result
        !result.message
        !result.error

        cleanup:
        userInSession?.delete(flush:true)
        userToDelete?.delete(flush:true)
        recommendedUserInfo?.delete(flush:true)
    }

}
