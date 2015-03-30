package kuorum.register

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.ui.RegistrationCode
import kuorum.core.exception.KuorumException
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.mail.KuorumMailService
import kuorum.users.KuorumUser
import kuorum.users.RoleUser
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.LocaleResolver
import springSecurity.KuorumRegisterCommand

class RegisterService {


    LinkGenerator grailsLinkGenerator

    KuorumMailService kuorumMailService

    SpringSecurityService springSecurityService

    public static final PREFIX_PASSWORD = "*registerUser*"

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
    @Transactional
    KuorumUser registerUser(KuorumRegisterCommand command){
        KuorumUser user = createUser(command)

        RegistrationCode registrationCode = registerUserCode(user)
        log.info("Usuario $user.name creado con el token  $registrationCode.token")
        if (registrationCode == null || registrationCode.hasErrors()) {
            throw new KuorumException("Error creando usuario")
        }

        String url = generateLink('verifyRegistration', [t: registrationCode.token])
        kuorumMailService.sendRegisterUser(user,url)

        if (!user.password){
            user.password = "${PREFIX_PASSWORD}${Math.random()}"
            user.save()
        }
        springSecurityService.reauthenticate user.email
        user
    }

    /*
        Convert the command object to User
    */

    KuorumUser createUser (KuorumRegisterCommand command){
        Locale locale = LocaleContextHolder.getLocale();
        AvailableLanguage availableLanguage = AvailableLanguage.fromLocaleParam(locale.getLanguage());
        KuorumUser user
            user = new KuorumUser(
                    email: command.email.toLowerCase(),
                    name: command.name,
                    language: availableLanguage,
                    accountLocked: true, enabled: true)
            user.relevantCommissions = CommissionType.values()
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
        RoleUser roleUser = RoleUser.findByAuthority("ROLE_PASSWORDCHANGED")
        user.authorities = [roleUser]
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

    RegistrationCode findOrRegisterUserCode(KuorumUser user) {
        RegistrationCode.findByUsername(user.email)?:registerUserCode(user)
    }

    protected String generateLink(String action, linkParams) {
        grailsLinkGenerator.link(absolute: true,
                controller: 'register', action: action,
                params: linkParams)
    }
}