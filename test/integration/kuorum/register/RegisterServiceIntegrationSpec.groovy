package kuorum.register

import grails.plugin.springsecurity.ui.RegistrationCode
import grails.test.spock.IntegrationSpec
import kuorum.helper.IntegrationHelper
import kuorum.users.KuorumUser
import kuorum.users.RoleUser
import springSecurity.KuorumRegisterCommand

class RegisterServiceIntegrationSpec extends IntegrationSpec {

    RegisterService registerService

    def "test to check the save method if the user has not password" (){
        given:
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")
        user.authorities = [RoleUser.findByAuthority("ROLE_INCOMPLETE_USER")]
        user.password = null

        when: "we call the save method "
        Map result = registerService.save(user)

        then:"we get a new user"
        result
        result.user
        !result.password
        result.user.id == user.id
        result.user.name == user.name
        result.user.email == user.email
        user.authorities.id.contains(RoleUser.findByAuthority("ROLE_INCOMPLETE_USER").id)
        KuorumUser.get(user.id)
        KuorumUser.get(user.id).name == user.name
        KuorumUser.get(user.id).email == user.email

        cleanup:"Delete the user"
        KuorumUser.get(user.id).delete(flush:true)
    }

    def "test to check the save method if the user has been completed with the password" (){
        given:
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")

        when: "we call the save method "
        Map result = registerService.save(user)

        then:"we get a new user"
        result
        result.user
        result.user.id == user.id
        result.user.name == user.name
        result.user.email == user.email
        result.user.password == user.password
        KuorumUser.get(user.id)
        KuorumUser.get(user.id).name == user.name
        KuorumUser.get(user.id).email == user.email
        KuorumUser.get(user.id).password == user.password

        cleanup:"Delete the user"
        KuorumUser.get(user.id).delete(flush:true)
    }

    def "test to check the save method if the params are not correct anf fails the register" (){
        given:
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")
        user.name = null

        when: "we call the save method "
        Map result = registerService.save(user)

        then:"we have an error"
        result
        result.errorMsg
        result.errorMsg == ('admin.createUser.error')
        !KuorumUser.get(user.id)
    }

    def "test to check the createUser method giving it the KuorumRegisterCommand"(){
        given:
        KuorumRegisterCommand command = new KuorumRegisterCommand(email:'alberto.baron@salenda.es', name:'Alberto baron', conditions: false)

        when:"call to the correct action"
        KuorumUser user =  registerService.createUser(command)

        then:"we obtain the user Kuorum saved"
        user
        user.email
        user.email == command.email
        user.name
        user.name == command.name
        user.accountLocked
        user.enabled
        user.authorities
        user.authorities.id.contains(RoleUser.findByAuthority("ROLE_INCOMPLETE_USER").id)

        and:"The user is not found in database because it is not already saved"
        !KuorumUser.findByEmail(user.email)
    }

    def "test to check the createUserByRegistrationCode if the registrationCode exists and a user is associated"(){
        given:"a user"
        KuorumUser user = IntegrationHelper.createDefaultUser("alberto.baron@salenda.es")
        user.accountLocked = true
        user.save(flush:true)

        and:"a valid registration token"
        RegistrationCode registrationCode = new RegistrationCode(username: user.email)
        registrationCode.save(flush:true)

        when:"call to the correct action"
        KuorumUser savedUser =  registerService.createUserByRegistrationCode(registrationCode)

        then:"we obtain the KuorumUser saved"
        savedUser
        savedUser.name == user.name
        savedUser.email == user.email
        !savedUser.accountLocked
        KuorumUser.get(savedUser.id)
        !RegistrationCode.get(registrationCode.id)

        cleanup:
        KuorumUser.get(savedUser.id).delete(flush:true)
    }

    def "test to check the createUserByRegistrationCode if the registrationcode exists but the user associated doesn't exist"(){
        given:"a user"
        RegistrationCode registrationCode = new RegistrationCode(username: 'alberto.baron@salenda.es')
        registrationCode.save(flush:true)

        when:"call to the correct action"
        KuorumUser savedUser =  registerService.createUserByRegistrationCode(registrationCode)

        then:"we obtain the user Kuorum saved"
        !savedUser
    }
}