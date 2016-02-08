package kuorum.register
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.ui.RegistrationCode
import kuorum.core.exception.KuorumException
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.mail.KuorumMailService
import kuorum.notifications.NotificationService
import kuorum.post.Post
import kuorum.post.PostVoteService
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.RoleUser
import kuorum.web.commands.customRegister.ContactRegister
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.transaction.annotation.Transactional
import springSecurity.KuorumRegisterCommand

class RegisterService {

    def grailsApplication

    LinkGenerator grailsLinkGenerator

    KuorumMailService kuorumMailService

    SpringSecurityService springSecurityService

    PostVoteService postVoteService

    KuorumUserService kuorumUserService

    NotificationService notificationService

    IndexSolrService indexSolrService

    public static final PREFIX_PASSWORD = "*registerUser*"

    private static final String META_DATA_REGISTER_VOTING_POST="votingPost"
    private static final String META_DATA_REGISTER_VOTING_POST_POST="postId"
    private static final String META_DATA_REGISTER_VOTING_POST_ANONYMOUS="anonymousVote"

    private static final String META_DATA_REGISTER_CONCATC_POLITICIAN="contactPolitician"
    private static final String META_DATA_REGISTER_CONCATC_POLITICIAN_ID="politicianId"
    private static final String META_DATA_REGISTER_CONCATC_POLITICIAN_MESSAGE="politicianMessage"
    private static final String META_DATA_REGISTER_CONCATC_POLITICIAN_CAUSE="politicianCause"

    public static final String NOT_USER_PASSWORD = "NO_VALID_PASS"
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
            RegistrationCode registrationCode = RegistrationCode.findByUsername(user."$usernameFieldName")
            if (!registrationCode){
                registrationCode = new RegistrationCode(username: user."$usernameFieldName")
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

        sendVerificationMail(user, 'verifyRegistration')

        if (!user.password){
            user.password = "${PREFIX_PASSWORD}${Math.random()}"
            user.save()
        }
        springSecurityService.reauthenticate user.email
        user
    }

    public void sendVerificationMail(KuorumUser user, String actionLink){
        RegistrationCode registrationCode = registerUserCode(user)
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
    KuorumUser registerUserVotingPost(KuorumRegisterCommand command, Post post, Boolean anonymousVote){
        KuorumUser user = registerUser(command);
        registerUserVotingPost(user, post, anonymousVote)
    }

    @Transactional
    KuorumUser registerUserVotingPost(KuorumUser user, Post post, Boolean anonymousVote){
        String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
        RegistrationCode registrationCode = RegistrationCode.findByUsername(user."$usernameFieldName")
        registrationCode[META_DATA_REGISTER_VOTING_POST] = ["$META_DATA_REGISTER_VOTING_POST_POST":post.id, "$META_DATA_REGISTER_VOTING_POST_ANONYMOUS":anonymousVote]
        registrationCode.save()
        user
    }
    @Transactional
    KuorumUser registerUserContactingPolitician(ContactRegister command){
        KuorumUser user = registerUser(command);
        String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
        RegistrationCode registrationCode = RegistrationCode.findByUsername(user."$usernameFieldName")
        registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN] = [
                "$META_DATA_REGISTER_CONCATC_POLITICIAN_ID":command.politician.id,
                "$META_DATA_REGISTER_CONCATC_POLITICIAN_MESSAGE":command.message,
                "$META_DATA_REGISTER_CONCATC_POLITICIAN_CAUSE":command.cause
        ]
        registrationCode.save()
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

    KuorumUser createUser(String name, String password, String email, String alias, AvailableLanguage lang, UserType userType){
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
            if (userType == UserType.POLITICIAN){
                user.authorities << RoleUser.findByAuthority("ROLE_POLITICIAN")
            }
            user.userType = userType
            user.save();
        }
        user;
    }

    String generateNotSetUserPassword(String prefix){
        "${NOT_USER_PASSWORD}_${prefix}_${Math.random()}"
    }

    boolean isPasswordSetByUser(KuorumUser user){
        isPasswordSetByUser(user.password)
    }
    boolean isPasswordSetByUser(String encodedPassword){
        return !(!encodedPassword
                || encodedPassword.startsWith(FacebookAuthService.NOT_USER_PASSWORD)
                || encodedPassword.startsWith(FacebookAuthService.PASSWORD_PREFIX)
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

    KuorumUser createUserByRegistrationCode (RegistrationCode registrationCode){
        KuorumUser user
        RegistrationCode.withTransaction { status ->
            user = KuorumUser.findByEmail(registrationCode.username)
            if (!user) {
                return
            }
            user.accountLocked = false
            user.save(flush:true)
            processMetaDataRegistration(user, registrationCode)
            registrationCode.delete(flush:true)
        }
        if (user) {
            kuorumMailService.mailingListUpdateUser(user)
        }
        try{
            indexSolrService.index(user)
        }catch(Exception e){
            log.error("Error indexando usuario",e)
        }
        user
    }
    private void processMetaDataRegistration(KuorumUser user, RegistrationCode registrationCode){
        if(registrationCode[META_DATA_REGISTER_VOTING_POST]){
            Post post = Post.get(registrationCode[META_DATA_REGISTER_VOTING_POST][META_DATA_REGISTER_VOTING_POST_POST])
            Boolean anonymous = registrationCode[META_DATA_REGISTER_VOTING_POST][META_DATA_REGISTER_VOTING_POST_ANONYMOUS]
            postVoteService.votePost(post, user, anonymous)
        }
        if(registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN]){
            KuorumUser politician = KuorumUser.get(registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN][META_DATA_REGISTER_CONCATC_POLITICIAN_ID])
            String message = registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN][META_DATA_REGISTER_CONCATC_POLITICIAN_MESSAGE]
            String cause = registrationCode[META_DATA_REGISTER_CONCATC_POLITICIAN][META_DATA_REGISTER_CONCATC_POLITICIAN_CAUSE]
            notificationService.sendPoliticianContactNotification(politician, user, message, cause)
        }
    }

    Map save(KuorumUser user) {
        def result
        kuorumUserService.modifyRoleDependingOnUserData(user)
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