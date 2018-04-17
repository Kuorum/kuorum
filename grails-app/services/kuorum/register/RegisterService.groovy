package kuorum.register

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.SpringSecurityUiService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.mail.KuorumMailService
import kuorum.notifications.NotificationService
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.RoleUser
import kuorum.web.commands.customRegister.ContactRegister
import kuorum.web.constants.WebConstants
import kuorum.web.users.KuorumRegistrationCode
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.transaction.annotation.Transactional
import payment.contact.ContactService
import springSecurity.KuorumRegisterCommand

class RegisterService {

    def grailsApplication

    LinkGenerator grailsLinkGenerator

    KuorumMailService kuorumMailService

    SpringSecurityService springSecurityService

    KuorumUserService kuorumUserService

    NotificationService notificationService

    IndexSolrService indexSolrService

    ContactService contactService

    def saltSource

    SpringSecurityUiService springSecurityUiService

    public static final PREFIX_PASSWORD = "*registerUser*"

    private static final String META_DATA_REGISTER_CONCATC_POLITICIAN="contactPolitician"
    private static final String META_DATA_REGISTER_CONCATC_POLITICIAN_ID="politicianId"
    private static final String META_DATA_REGISTER_CONCATC_POLITICIAN_MESSAGE="politicianMessage"
    private static final String META_DATA_REGISTER_CONCATC_POLITICIAN_CAUSE="politicianCause"

    private static final String META_DATA_REGISTER_FOLLOW_POLITICIAN="followPolitician"
    private static final String META_DATA_REGISTER_FOLLOW_POLITICIAN_ID="followPoliticianId"

    @Deprecated
    public static final String NOT_USER_PASSWORD = "NO_VALID_PASS"
    /*
        Action to register the name of a new user and generate the token to the Link
     */
    @Transactional
    KuorumRegistrationCode registerUserCode(user) {
        String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
        KuorumRegistrationCode.withTransaction { status->
            if (!user.save()) {
                log.error "Error registering a new user : ${user}"
                status.setRollbackOnly()
                return null
            }
            KuorumRegistrationCode registrationCode = KuorumRegistrationCode.findByUsername(user."$usernameFieldName")
            if (!registrationCode){
                registrationCode = new KuorumRegistrationCode(username: user."$usernameFieldName")
            }
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

        sendVerificationMail(user, 'verifyRegistration', command.redirectUrl)

        if (!user.password){
            user.password = "${PREFIX_PASSWORD}${Math.random()}"
            indexSolrService.deltaIndex()
            user.save()
        }
        springSecurityService.reauthenticate user.email
        user
    }

    public void sendVerificationMail(KuorumUser user, String actionLink, String redirectLink = null){
        KuorumRegistrationCode registrationCode = registerUserCode(user)
        if (redirectLink){
            registrationCode.redirectLink= redirectLink
            registrationCode.save()
        }
        log.info("Usuario $user.name creado con el token  $registrationCode.token")
        if (registrationCode == null || registrationCode.hasErrors()) {
            throw new KuorumException("Error creando usuario")
        }

        String url = generateLink(actionLink, [t: registrationCode.token])
        kuorumMailService.sendRegisterUser(user,url)
    }

    def changeEmail(KuorumUser user, String newEmail){

    }

    @Transactional
    KuorumUser registerUserContactingPolitician(ContactRegister command){
        KuorumUser user = registerUser(command);
        String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
        KuorumRegistrationCode registrationCode = KuorumRegistrationCode.findByUsername(user."$usernameFieldName")
        registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN] = [
                "$META_DATA_REGISTER_CONCATC_POLITICIAN_ID":command.politician.id,
                "$META_DATA_REGISTER_CONCATC_POLITICIAN_MESSAGE":command.message,
                "$META_DATA_REGISTER_CONCATC_POLITICIAN_CAUSE":command.cause
        ]
        registrationCode.save()
        user
    }

    @Transactional
    KuorumUser registerUserFollowingPolitician(KuorumRegisterCommand command, KuorumUser following){
        KuorumUser user = registerUser(command);
        String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
        KuorumRegistrationCode registrationCode = KuorumRegistrationCode.findByUsername(user."$usernameFieldName")
        registrationCode[META_DATA_REGISTER_FOLLOW_POLITICIAN] = ["$META_DATA_REGISTER_FOLLOW_POLITICIAN_ID":following.id]
        registrationCode.save()

        //Sets default pass and alias to delete custom register process
        user.password = "${PREFIX_PASSWORD}_${Math.random()}"
        user.alias = user.id.toString().take(15)
        user.save()
    }

    /*
        Convert the command object to User
    */

    KuorumUser createUser (KuorumRegisterCommand command){
        Locale locale = LocaleContextHolder.getLocale();
        AvailableLanguage availableLanguage = AvailableLanguage.fromLocaleParam(locale.getLanguage());
        if (!availableLanguage ){
            availableLanguage = AvailableLanguage.en_EN
        }
        String alias = kuorumUserService.generateValidAlias(command.name)
        KuorumUser user
            user = new KuorumUser(
                    email: command.email.toLowerCase(),
                    name: command.name,
                    language: availableLanguage,
                    domain: CustomDomainResolver.domain,
                    accountLocked: false, enabled: true)
            user.relevantCommissions = CommissionType.values()
            user.authorities = [RoleUser.findByAuthority("ROLE_INCOMPLETE_USER")]
            user.alias = alias
            user
    }

    KuorumUser createUser(String name, String password, String email, String alias, AvailableLanguage lang){
        KuorumUser user
        KuorumUser.withNewTransaction {status->

            user = new KuorumUser(
                    email: email.toLowerCase(),
                    name: name,
                    language: lang,
                    password: password,
                    alias:alias,
                    accountLocked: false, enabled: false)
            user.relevantCommissions = CommissionType.values()
            user.authorities = [RoleUser.findByAuthority("ROLE_INCOMPLETE_USER")]
            user.save();
        }
        user;
    }

    String generateNotSetUserPassword(String prefix){
        "${PREFIX_PASSWORD}_${prefix}_${Math.random()}"
    }

    boolean isPasswordSetByUser(KuorumUser user){
        isPasswordSetByUser(user.password)
    }
    boolean isPasswordSetByUser(String encodedPassword){
        return !(!encodedPassword
                || encodedPassword.startsWith(NOT_USER_PASSWORD)
                || encodedPassword.startsWith(PREFIX_PASSWORD)
                || encodedPassword.startsWith(FacebookOAuthService.PASSWORD_PREFIX)
                || encodedPassword.startsWith(GoogleOAuthService.PASSWORD_PREFIX))

    }

    Boolean checkValidEmail(String email){
        def mailParts = email.split("@")
        if (mailParts.size() == 2){
            def domain = mailParts[1]
            def notAllowed = grailsApplication.config.kuorum.register.notAllowedTemporalDomainEmails.find{"@$domain".equalsIgnoreCase(it)}
            if (notAllowed){
                return false
            }
            return true;
        }else{
            return false;
        }
    }

    String encodePassword(KuorumUser user, String rawPass){
        String salt = saltSource instanceof NullSaltSource ? null : user.name
        springSecurityUiService.encodePassword(rawPass, salt)
    }
    Boolean isValidPassword(KuorumUser user, String rawPass){
        if (user){
            String salt = saltSource instanceof NullSaltSource ? null : user.name
            return springSecurityService.passwordEncoder.isPasswordValid(user.password, rawPass, salt)
        }else{
            return false;
        }
    }

    KuorumUser createUserByRegistrationCode (KuorumRegistrationCode registrationCode){
        KuorumUser user
        KuorumRegistrationCode.withTransaction { status ->
            user = KuorumUser.findByEmailAndDomain(registrationCode.username, CustomDomainResolver.domain)
            if (!user) {
                return
            }
            user.accountLocked = false
            RoleUser incompleteRoleUser = RoleUser.findByAuthority("ROLE_INCOMPLETE_USER")
            RoleUser normalRoleUser = RoleUser.findByAuthority("ROLE_USER")
            user.authorities.remove(incompleteRoleUser)
            user.authorities.add(normalRoleUser)
            user.save(flush:true)
            processMetaDataRegistration(user, registrationCode)
            registrationCode.delete(flush:true)
        }
        if (user) {
            kuorumMailService.mailingListUpdateUser(user)
            followMainUser(user)
        }
        try{
            indexSolrService.deltaIndex()
        }catch(Exception e){
            log.error("Error indexando usuario",e)
        }
        springSecurityService.reauthenticate user.email
        user
    }
    private void processMetaDataRegistration(KuorumUser user, KuorumRegistrationCode registrationCode){

        if(registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN]){
            KuorumUser politician = KuorumUser.get(registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN][META_DATA_REGISTER_CONCATC_POLITICIAN_ID])
            String message = registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN][META_DATA_REGISTER_CONCATC_POLITICIAN_MESSAGE]
            String cause = registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN][META_DATA_REGISTER_CONCATC_POLITICIAN_CAUSE]
            notificationService.sendPoliticianContactNotification(politician, user, message, cause)
        }
        if(registrationCode[META_DATA_REGISTER_FOLLOW_POLITICIAN]){
            KuorumUser following = KuorumUser.get(registrationCode[META_DATA_REGISTER_FOLLOW_POLITICIAN][META_DATA_REGISTER_FOLLOW_POLITICIAN_ID])
            kuorumUserService.createFollower(user, following)
        }
    }

    private void followMainUser(KuorumUser user){
        // TODO: This logic should be on API. First is necessary to clarify which will be the process
        KuorumUser userAdmin = kuorumUserService.findByAlias(WebConstants.FAKE_LANDING_ALIAS_USER)
        if (userAdmin){
            kuorumUserService.createFollower(user, userAdmin)
        }else{
            log.info("No admin user defined for domain: ${CustomDomainResolver.domain}")
        }

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

    KuorumRegistrationCode findOrRegisterUserCode(KuorumUser user) {
        KuorumRegistrationCode.findByUsername(user.email)?:registerUserCode(user)
    }

    protected String generateLink(String action, linkParams) {
        grailsLinkGenerator.link(absolute: true,
                controller: 'register', action: action,
                params: linkParams)
    }
}