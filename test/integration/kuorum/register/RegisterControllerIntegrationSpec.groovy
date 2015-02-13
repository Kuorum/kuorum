package kuorum.register

import grails.plugin.springsecurity.ui.RegistrationCode
import grails.test.spock.IntegrationSpec
import kuorum.helper.IntegrationHelper
import kuorum.users.KuorumUser
import kuorum.users.RoleUser
import spock.lang.Shared
import springSecurity.KuorumRegisterCommand
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

    void "test register"() {
        given:"a register object"
        KuorumRegisterCommand kuorumRegisterCommand = new KuorumRegisterCommand(email:'alberto.baron@salenda.es', name:'Alberto baron', conditions: false)

        when:"call to the correct action"
        registerController.register(kuorumRegisterCommand)

        then:"go to the correct page"
        redirectMap
        redirectMap.mapping
        redirectMap.mapping == 'registerSuccess'
        KuorumUser.findByEmail(kuorumRegisterCommand.email).authorities.id.contains(RoleUser.findByAuthority("ROLE_INCOMPLETE_USER").id)

        cleanup:
        KuorumUser.findByEmail(kuorumRegisterCommand.email)?.delete(flush:true)
    }

    void "test choosePassword with different passwords"() {
        given:"a user to assign the password and different passwords in the params"
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")
        user.authorities = [RoleUser.findByAuthority("ROLE_INCOMPLETE_USER")]
        user.password = null
        user.save(flush:true)
        registerController.params.password1 = 'password1'
        registerController.params.password2 = 'another_pass'
        registerController.params.userId = user.id

        when:"get the user in the database"
        KuorumUser userInDatabase = KuorumUser.get(registerController.params.userId)

        then:"the user has not password"

        !userInDatabase.password

        when:"call to the action"
        registerController.choosePassword()

        then:"check that goes to the correct view giving it the user to get it and show a flash message, so the password is not saved"
        renderMap
        renderMap.view
        renderMap.view == 'choosePass'
        renderMap.model
        renderMap.model.userId
        renderMap.model.userId == registerController.params.userId
        registerController
        registerController.flash
        registerController.flash.typeMessage
        registerController.flash.message
        registerController.flash.message.contains('La contraseña ha de ser identica a la primera para comprobar que se ha escrito correctamente')
        KuorumUser.findByEmail(user.email)
        user.authorities.id.contains(RoleUser.findByAuthority("ROLE_INCOMPLETE_USER").id)
        !userInDatabase.password

        cleanup:
        KuorumUser.get(user.id)?.delete(flush:true)
    }

    void "test choosePassword with the same password"() {
        given:"a user to assign the password and the same password by params"
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")
        user.authorities = [RoleUser.findByAuthority("ROLE_INCOMPLETE_USER")]
        user.password = null
        user.save(flush: true)
        registerController.params.password1 = 'password1'
        registerController.params.password2 = 'password1'
        registerController.params.userId = user.id

        when:"get the user"
        KuorumUser userInDatabase = KuorumUser.get(user.id)

        then:"check that it has not password"
        !userInDatabase.password

        when:"call to the action"
        registerController.choosePassword()

        then:"Check that the user has a new password, the role is incomplete and show a successfully message"
        redirectMap
        registerController.flash
        registerController.flash.message
        registerController.flash.message.contains('Your registration is complete')
        redirectMap.uri
        userInDatabase.password
        userInDatabase.password == registerController.params.password1
        userInDatabase.authorities.id.contains(RoleUser.findByAuthority("ROLE_INCOMPLETE_USER").id)

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
        registerController.flash.error == 'Código incorrecto o ya utilizado'
        redirectMap
        redirectMap.uri
        redirectMap.uri == defaultTarget
        !KuorumUser.findByEmail('alberto.baron@salenda.es')
    }


    void "test verifyRegistration if there is a successfully token"() {
        given:"a user"
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")
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
        renderMap.view == 'choosePass'
        renderMap.model
        renderMap.model.userId
        !RegistrationCode.get(registrationCode.id)
        !KuorumUser.findByEmail(user.email).accountLocked

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
        registerController.flash.error == 'El usuario no ha sido encontrado'
        redirectMap
        redirectMap.uri
        redirectMap.uri == "/"
        !KuorumUser.findByEmail('alberto.baron@salenda.es')
    }
}
