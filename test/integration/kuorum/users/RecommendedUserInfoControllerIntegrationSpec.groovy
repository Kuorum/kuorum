package kuorum.users

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.test.mixin.TestFor
import kuorum.helper.IntegrationHelper
import org.springframework.context.MessageSource
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Specification

@TestFor(RecommendedUserInfo)
class RecommendedUserInfoControllerIntegrationSpec extends Specification{

    @Shared
    MessageSource messageSource

    @Shared
    def redirectMap

    @Shared
    def renderMap

    @Shared
    RecommendedUserInfoController recommendedUserInfoController

    void setupSpec(){
        RecommendedUserInfoController.metaClass.redirect = { Map map ->
            redirectMap = map
        }
        RecommendedUserInfoController.metaClass.render = { Map map ->
            renderMap = map
        }
        recommendedUserInfoController = new RecommendedUserInfoController()
    }

    def "test to check if a validate user can delete recommended user"(){
        given:
        KuorumUser userInSession = (IntegrationHelper.createDefaultUser("userInSession@example.es")).save(flush:true)
        KuorumUser userToDelete = (IntegrationHelper.createDefaultUser("userToDelete@example.es")).save(flush:true)

        recommendedUserInfoController.params.deletedUserId = userToDelete.id

        when:
        SpringSecurityUtils.doWithAuth(userInSession.email) {
            recommendedUserInfoController.deleteRecommendedUser()
        }

        then:
        recommendedUserInfoController
        recommendedUserInfoController.flash
        recommendedUserInfoController.flash.message
        recommendedUserInfoController.flash.message == messageSource.getMessage('recommendedUserInfoService.addUserToDelete.savingDeleteUserSuccessfully', null, recommendedUserInfoController.request.locale)

        cleanup:
        userInSession?.delete(flush:true)
        userToDelete?.delete(flush:true)
    }


    def "test to check if a non validate user can delete recommended user"(){
        given:
        KuorumUser userInSession = (IntegrationHelper.createDefaultUser("userInSession@example.es")).save(flush:true)

        recommendedUserInfoController.params.deletedUserId = null

        when:
        SpringSecurityUtils.doWithAuth(userInSession.email) {
            recommendedUserInfoController.deleteRecommendedUser()
        }

        then:
        recommendedUserInfoController
        recommendedUserInfoController.flash
        recommendedUserInfoController.flash.error
        recommendedUserInfoController.flash.error == messageSource.getMessage('recommendedUserInfoService.addUserToDelete.errorValidatingDeleteUser', null, recommendedUserInfoController.request.locale)

        cleanup:
        userInSession?.delete(flush:true)
    }
}
