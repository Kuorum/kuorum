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
        def result
        SpringSecurityUtils.doWithAuth(userInSession.email) {
            result = recommendedUserInfoController.deleteRecommendedUser()
        }

        then:
        recommendedUserInfoController.response.json
        !recommendedUserInfoController.response.json.error
        !recommendedUserInfoController.response.json.message

        cleanup:
        userInSession?.delete(flush:true)
        userToDelete?.delete(flush:true)
    }


    def "test to check if a non validate user can delete recommended user"(){
        given:
        KuorumUser userInSession = (IntegrationHelper.createDefaultUser("userInSession@example.es")).save(flush:true)

        recommendedUserInfoController.params.deletedUserId = null

        when:
        def result
        SpringSecurityUtils.doWithAuth(userInSession.email) {
            result = recommendedUserInfoController.deleteRecommendedUser()
        }

        then:
        recommendedUserInfoController.response.json
        recommendedUserInfoController.response.json.error
        recommendedUserInfoController.response.json.message == messageSource.getMessage('recommendedUserInfoService.addUserToDelete.errorValidatingDeleteUser', null, recommendedUserInfoController.request.locale)

        cleanup:
        userInSession?.delete(flush:true)
    }
}
