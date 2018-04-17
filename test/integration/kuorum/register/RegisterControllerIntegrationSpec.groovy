package kuorum.register

import grails.plugin.springsecurity.ui.RegistrationCode
import grails.test.spock.IntegrationSpec
import kuorum.helper.IntegrationHelper
import kuorum.users.KuorumUser
import kuorum.users.RoleUser
import spock.lang.Shared
import springSecurity.RegisterController

class RegisterControllerIntegrationSpec extends IntegrationSpec {

    @Shared
    RegisterController registerController

    @Shared
    def redirectMap

    @Shared
    def renderMap

    def setup() {
        RegisterController.metaClass.redirect = { Map map ->
            redirectMap = map
        }
        RegisterController.metaClass.render = { Map map ->
            renderMap = map
        }
        registerController = new RegisterController()
    }


    void "test selectMyPassword with different passwords"() {
        given:"a user to assign the password and different passwords in the params"
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")
        user.authorities = [RoleUser.findByAuthority("ROLE_INCOMPLETE_USER")]
        user.password = null
        user.save(flush:true)
        registerController.params.password = 'password1'
        registerController.params.password2 = 'another_pass'
        registerController.params.userId = user.id

        when:"get the user in the database"
        KuorumUser userInDatabase = KuorumUser.get(registerController.params.userId)

        then:"the user has not password"

        !userInDatabase.password

        when:"call to the action"
        registerController.selectMyPassword()

        then:"check that goes to the correct view giving it the user to get it and show a flash message, so the password is not saved"
        renderMap
        renderMap.view
        renderMap.view == 'selectMyPassword'
        renderMap.model
        renderMap.model.userId
        renderMap.model.userId == registerController.params.userId
        renderMap.model.command
        renderMap.model.command.errors
        KuorumUser.findByEmailAndDomain(user.email, "test.kuorum.org")
        user.authorities.id.contains(RoleUser.findByAuthority("ROLE_INCOMPLETE_USER").id)
        !userInDatabase.password

        cleanup:
        KuorumUser.get(user.id)?.delete(flush:true)
    }

    void "test selectMyPassword with the same password"() {
        given:"a user to assign the password and the same password by params"
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")
        user.personalData.provinceCode = null
        user.personalData.province = null
        user.authorities = [RoleUser.findByAuthority("ROLE_INCOMPLETE_USER")]
        user.password = null
        user.save(flush: true)
        registerController.params.password = 'password1'
        registerController.params.password2 = 'password1'
        registerController.params.userId = user.id

        when:"get the user"
        KuorumUser userInDatabase = KuorumUser.get(user.id)

        then:"check that it has not password"
        !userInDatabase.password

        when:"call to the action"
        registerController.selectMyPassword()

        then:"Check that the user has a new password, the role is incomplete and show a successfully message"
        redirectMap
        registerController.flash
        registerController.flash.message
        redirectMap.uri
        userInDatabase.password
        userInDatabase.authorities.id.contains(RoleUser.findByAuthority("ROLE_PASSWORDCHANGED").id)

        cleanup:
        KuorumUser.get(user.id)?.delete(flush:true)
    }

    void "test verifyRegistration if there is not a successfully token"() {
        given:"a user"
        IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")
        String defaultTarget = "/"

        and:"an invalid token"
        registerController.params.t = 'b2d069b7fd99447989c482d0949516d6'

        when:"call to the method"
        registerController.verifyRegistration()

        then:"It will receive an error message and the uri will be the default uri. Moreover, the user won't be saved"
        registerController
        registerController.flash
        registerController.flash.error
        redirectMap
        redirectMap.uri
        redirectMap.uri == defaultTarget
        !KuorumUser.findByEmailAndDomain('alberto.baron@salenda.es', 'test.kuorum.org')
    }


    void "test verifyRegistration if there is a successfully token"() {
        given:"a user"
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es", 'test.kuorum.org')
        user.save(flush:true)

        and:"a valid registration token"
        RegistrationCode registrationCode = new RegistrationCode(username: user.email)
        registrationCode.save(flush:true)
        registerController.params.t = registrationCode.token

        when:"call to the method"
        registerController.verifyRegistration()

        then:"The user will be saved with accountLocked to false, the registrationCode will be deleted, and the user will be redirect to a new view"
        renderMap
        renderMap.view
        renderMap.view == 'selectMyPassword'
        renderMap.model
        renderMap.model.userId
        !RegistrationCode.get(registrationCode.id)
        !KuorumUser.findByEmailAndDomain(user.email, "test.kuorum.org").accountLocked

        cleanup:
        KuorumUser.get(user.id)?.delete(flush:true)
    }

    void "test verifyRegistration if there is a successfully token but the user is not found in database"() {
        given:"a valid registration token"
        RegistrationCode registrationCode = new RegistrationCode(username: 'alberto.baron@salenda.es')
        registrationCode.save(flush:true)
        registerController.params.t = registrationCode.token

        when:"call to the method"
        registerController.verifyRegistration()

        then:"The user related to RegistrationCode doesn't exist"
        registerController
        registerController.flash
        registerController.flash.error
        redirectMap
        redirectMap.uri
        redirectMap.uri == "/"
        !KuorumUser.findByEmailAndDomain('alberto.baron@salenda.es', 'test.kuorum.org')
    }
}
