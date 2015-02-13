package kuorum.register

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.ui.RegistrationCode
import kuorum.users.KuorumUser
import kuorum.users.RoleUser
import org.springframework.transaction.annotation.Transactional
import springSecurity.KuorumRegisterCommand

class RegisterService {

    /*
        Action to register the name of a new user and generate the token to the Link
     */
    @Transactional
    RegistrationCode registerUserCode(user) {
        String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
        RegistrationCode.withTransaction {status->
            if (!user.save()) {
                log.error "Error registering a new user : ${user}"
                status.setRollbackOnly()
                return null
            }

            def registrationCode = new RegistrationCode(username: user."$usernameFieldName")
            if (!registrationCode.save()) {
                log.error "Error saving a registrationCode : ${registrationCode}"
                status.setRollbackOnly()
            }
            registrationCode
        }
    }

    /*
        Convert the command object to User
    */

    KuorumUser createUser (KuorumRegisterCommand command){
        KuorumUser user
            user = new KuorumUser(
                    email: command.email.toLowerCase(),
                    name: command.name,
                    accountLocked: true, enabled: true)
            user.relevantCommissions = []
            user.authorities = [RoleUser.findByAuthority("ROLE_INCOMPLETE_USER")]
            user
    }


    KuorumUser createUserByRegistrationCode (RegistrationCode registrationCode){
        KuorumUser user
        RegistrationCode.withTransaction { status ->
            user = KuorumUser.findByEmail(registrationCode.username)
            if (!user) {
                return
            }
            user.accountLocked = false
            user.save(flush:true)
            registrationCode.delete(flush:true)
        }
        user
    }

    Map save(KuorumUser user) {
        def result
        if (user.validate()) {
            try {
                if (user.save()) {
                    result = [user: user]
                } else {
                    result = [errorMsg: 'admin.createUser.errorSaved']
                }
            } catch (e) {
                result = [errorMsg: 'admin.createUser.errorSaved']
                log.error "Error saving a user object : ${e.message}"
            }
        } else {
            result = [errorMsg: 'admin.createUser.error']
        }
        result
    }
}
