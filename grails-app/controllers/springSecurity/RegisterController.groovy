package springSecurity

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.plugin.springsecurity.ui.ResetPasswordCommand
import grails.plugin.springsecurity.ui.SpringSecurityUiService
import grails.validation.Validateable
import kuorum.captcha.CaptchaService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
import kuorum.files.FileService
import kuorum.notifications.NotificationService
import kuorum.register.IOAuthService
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.solr.IndexSolrService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.customRegister.ForgotUserPasswordCommand
import kuorum.web.users.KuorumRegistrationCode
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.survey.SurveyRSDTO
import org.kuorum.rest.model.domain.DomainValidationRDTO
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.RememberMeServices
import payment.campaign.CampaignService
import payment.campaign.SurveyService

class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {

    def kuorumMailService
    RegisterService registerService

    KuorumUserService kuorumUserService
    SpringSecurityService springSecurityService
    SpringSecurityUiService springSecurityUiService
    NotificationService notificationService
    IndexSolrService indexSolrService
    CampaignService campaignService

    FileService fileService
    CookieUUIDService cookieUUIDService
    DomainService domainService
    SurveyService surveyService
    CaptchaService captchaService


    def index() {
        def copy = [:] + (flash.chainedParams ?: [:])
        copy.remove 'controller'
        copy.remove 'action'
        def config = SpringSecurityUtils.securityConfig

        checkRegisterActiveAndRedirect();

        if (springSecurityService.isLoggedIn()) {
            redirect uri: config.successHandler.defaultTargetUrl
//            flash.message = message(code:'register.alreadyRegistered')
            return
        }
        [command: new KuorumRegisterCommand(copy)]
    }

    private void checkRegisterActiveAndRedirect() {
        if (domainService.getConfig(CustomDomainResolver.domain).getShowRegisterButton()) {
        } else {
            log.info("Trying to register with register button hidden")
            flash.error = g.message(code: 'head.noLogged.register.disabledButton')
            redirect uri: g.createLink(mapping: "home", params: [evictCache: "true"])
            return
        }
    }

    def register(KuorumRegisterCommand command) {
        registerUser(command, "customProcessRegisterStep2")
    }

    private void registerUser(KuorumRegisterCommand command, String redirectMapping, def paramsMapping = [:]) {
        if (command.hasErrors()) {
            render view: 'index', model: [command: command]
            return
        }
        if (grails.util.Environment.current == grails.util.Environment.PRODUCTION && !verifyRegister()) {
            render([success: false] as JSON)
            return
        }

        KuorumUser user = registerService.registerUser(command)
        redirect mapping: redirectMapping, params: paramsMapping
    }

    def verifyRegister() {
        return captchaService.verifyCaptcha(params['g-recaptcha-response'])
    }

    RememberMeServices rememberMeServices

    def registerRRSSOAuthAjax() {
        params.remove("controller")
        params.remove("action")
        params.remove("language")
        String providerName = params.remove("provider")
        String redirectAdminConfig = params.remove("redirectAdminConfig")
        // This params is used from kuorum.org to redirect first time to domain configuration
        IOAuthService providerService = grailsApplication.mainContext.getBean("${providerName}OAuthService")
        org.scribe.model.Token token = providerService.createTokenFromAjaxParams(params)
        if (!token) {
            flash.message = "This code is already used or not exists"
            redirect mapping: 'home'
            return
        }
        grails.plugin.springsecurity.oauth.OAuthToken oAuthToken = providerService.createAuthToken(token)
        oAuthToken.authenticated = true
        SecurityContextHolder.context.authentication = oAuthToken
        rememberMeServices.loginSuccess(request, response, springSecurityService.authentication)
        if (oAuthToken.newUser) {
            try {
                indexSolrService.deltaIndex()
            } catch (Exception e) {
                log.error("Error recovering and indexing new user. Reindex manually")
            }
        }
        if (redirectAdminConfig) {
            redirectInitialConfig(params)
        } else {
            render([result: "success"] as JSON)
        }
    }

    def testInitialConfig() {
        redirectInitialConfig(params)
    }

    private def redirectInitialConfig(def params) {
        if (domainService.isSurveyPlatform()) {
            KuorumUserSession userLogged = springSecurityService.principal
            log.info("Creating initial surver of domain ${CustomDomainResolver.domain} of user ${userLogged.alias}")
            SurveyRSDTO surveyRSDTO = surveyService.buildSurveyExample(userLogged)
            redirect mapping: 'surveyInitDomainEditQuestions', params: [campaignId: surveyRSDTO.getId()]
        } else {
            log.info("Redirecting for default configuration of the domain ${CustomDomainResolver.domain}")
            redirect mapping: 'politicianCampaigns', params: [tour: CustomDomainResolver.getDomainRSDTO().getTourEnabled()]
        }
    }

    def ajaxRegister(KuorumRegisterCommand command) {
        if (command.hasErrors()) {
            render([success: false, error: g.message(error: command.errors.getAllErrors().first())] as JSON)
            return
        }

        if (!verifyRegister()) {
            render([success: false, error: 'You looks like a robot'] as JSON)
            return
        }

        KuorumUser user = registerService.registerUser(command)
//        String automaticAlias = kuorumUserService.generateValidAlias(user.name)
//        kuorumUserService.updateAlias(user, automaticAlias)
//        kuorumUserService.updateUser(user)
        if (user) {
            render([success: true] as JSON)
        } else {
            render([success: false] as JSON)
        }
    }

    def checkEmail(String email) {
        KuorumUser user = KuorumUser.findByEmailAndDomain(email, CustomDomainResolver.domain)
        if (user) {
            render Boolean.FALSE.toString()
        } else {
            render Boolean.TRUE.toString()
        }
    }

    def registerSuccess() {}

    def resendRegisterVerification() {
        [command: new ResendVerificationMailCommand(email: params.email)]
    }

    def resendVerification(ResendVerificationMailCommand command) {
        KuorumUser user = KuorumUser.findByEmailAndDomain(command.email.toLowerCase(), CustomDomainResolver.domain)
        if (!user) {
            flash.error = message(code: 'springSecurity.ResendVerificationMailCommand.email.notUserExists')
            command.errors.rejectValue('email', 'springSecurity.ResendVerificationMailCommand.email.notUserExists')
        }
        KuorumRegistrationCode registrationCode = KuorumRegistrationCode.findByUsername(user.email)
        if (!registrationCode) {
            flash.error = message(code: 'springSecurity.ResendVerificationMailCommand.email.notUserExists')
            command.errors.rejectValue('email', 'springSecurity.ResendVerificationMailCommand.email.notToken')
        }
        if (command.hasErrors()) {
            render view: 'resendRegisterVerification', model: [command: command]
            return
        }

        log.info("Usuario $user.name recordando token  $registrationCode.token")
        String url = generateLinkWithMapping('registerVerifyAccount', [t: registrationCode.token])

        kuorumMailService.sendRegisterUser(user, url)
        flash.chainedParams = [link: url]
        flash.message = message(code: 'register.locked.resendSuccess')
        redirect mapping: "registerSuccess"
    }


    def sendConfirmationEmail() {
        KuorumUser user = KuorumUser.findById(springSecurityService.principal.id)
        String url = createLink(absolute: true, mapping: 'registerVerifyAccount', params: [t: params.t])
        kuorumMailService.sendRegisterUser(user, url)
        redirect mapping: 'home'
    }


    def verifyRegistration() {

        def conf = SpringSecurityUtils.securityConfig
        String redirectUrl = conf.successHandler.defaultTargetUrl

        String token = params.t

        KuorumRegistrationCode registrationCode = token ? KuorumRegistrationCode.findByToken(token) : null
        if (!registrationCode) {
            flash.error = message(code: 'spring.security.ui.register.badCode')
            redirect uri: redirectUrl
            return
        }

        KuorumUser user = registerService.createUserByRegistrationCode(registrationCode)

        if (!user) {
            flash.error = message(code: 'spring.security.ui.register.userNotFound')
            redirect uri: redirectUrl
            return
        }
        if (registerService.isPasswordSetByUser(user)) {
            redirect mapping: 'dashboard'
        } else {
            redirect mapping: 'customProcessRegisterStep2', params: [tour: CustomDomainResolver.getDomainRSDTO().getTourEnabled()]
        }

        // REDIRECT USING registrationCode redirectLink
//        render view:'selectMyPassword', model:[userId:user.id, command:new ResetPasswordCommand()]
//        if (registrationCode.redirectLink){
//            redirectUrl = registrationCode.redirectLink
//            redirect (uri:redirectUrl)
//            redirect mapping:'customProcessRegisterStep2', params: [tour:true]
//        }else{
//            redirect mapping:'customProcessRegisterStep2', params: [tour:true]
//        }
    }

    def ajaxValidationForgotPassword(ForgotUserPasswordCommand command) {

        if (command.hasErrors()) {
            render Boolean.FALSE.toString()
        } else {
            render Boolean.TRUE.toString()
        }
    }

    def forgotPassword() {
        [command: new ForgotUserPasswordCommand()]
    }
    def forgotPasswordPost = { ForgotUserPasswordCommand command ->

        if (command.hasErrors()) {
            render view: "forgotPassword", model: [command: command]
            return
        }

        KuorumUser user = KuorumUser.findByEmailAndDomain(command.email, CustomDomainResolver.domain)
        if (!user) {
            command.errors.rejectValue("email", "kuorum.web.commands.customRegister.ForgotUserPasswordCommand.email.invalid")
            render view: "forgotPassword", model: [command: command]
            return
        }

        def registrationCode = new RegistrationCode(username: command.email)
        registrationCode.save(flush: true)

        String url = generateLinkWithMapping('resetPasswordChange', [t: registrationCode.token])

        kuorumMailService.sendResetPasswordMail(user, url)

        redirect mapping: "resetPasswordSent"
    }

    def forgotPasswordSuccess = {}

    def resetPassword(ResetPasswordCommand command) {

        String token = params.t

        def registrationCode = token ? RegistrationCode.findByToken(token) : null
        if (!registrationCode) {
            flash.error = message(code: 'spring.security.ui.resetPassword.badCode')
            redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
            return
        }

        String ulrCallback = cookieUUIDService.getRememberPasswordRedirect()
        String urlCancelResetPassword = g.createLink(mapping: "login")
        if (!ulrCallback) {
            def conf = SpringSecurityUtils.securityConfig
            ulrCallback = conf.ui.register.postResetUrl ?: conf.successHandler.defaultTargetUrl
        } else {
            urlCancelResetPassword = ulrCallback
        }
        if (!request.post) {
            //Request change password via GET [Not form filled]
            return [token: token, command: new ResetPasswordCommand(), urlCancelResetPassword: urlCancelResetPassword]
        }

        command.username = registrationCode.username
        command.validate()

        if (command.hasErrors()) {
            return [token: token, command: command, urlCancelResetPassword: urlCancelResetPassword]
        }

        String salt = saltSource instanceof NullSaltSource ? null : registrationCode.username
        RegistrationCode.withTransaction { status ->
            KuorumUser user = KuorumUser.findByEmailAndDomain(registrationCode.username, CustomDomainResolver.domain)
            user.accountLocked = false
            user.password = springSecurityUiService.encodePassword(command.password, salt)
            user.save()
            registrationCode.delete()
        }

        springSecurityService.reauthenticate registrationCode.username

        flash.message = message(code: 'spring.security.ui.resetPassword.success')
        redirect uri: ulrCallback
    }

    def join() {
        if (!isQrEnableOnDomain()) {
            qrDisabledRedirect()
            return
        }
        CodeJoinCommand command = new CodeJoinCommand()
        if (flash.error && !command.hasErrors()) {
            // Error passed as param
            command.errors.rejectValue('qrCode', flash.error)
            flash.error = ""
        }
        return [command: command]
    }

    def joinPost(CodeJoinCommand command) {
        if (command.hasErrors()) {
            render view: 'join', model: [command: command]
            return
        }
        redirect mapping: 'joinDomainCheck', params: [qrCode: command.qrCode]
    }

    private boolean isQrEnableOnDomain() {
        CustomDomainResolver.getDomainRSDTO().getFirstFactorValidation() == DomainValidationRDTO.QR
    }

    def joinCheck() {
        if (!isQrEnableOnDomain()) {
            qrDisabledRedirect()
            return
        }
        CampaignRSDTO campaign = campaignService.findByQrCode(params.get("qrCode"))
        if (campaign) {
            ExternIdJoinCommand command = new ExternIdJoinCommand(campaignId: campaign.id, ownerId: campaign.user.id)
            return [command: command, labelExternalId: CustomDomainResolver.domainRSDTO.externalIdName, campaign: campaign]
        } else {
            flash.error = "springSecurity.join.qrCode.error"
            redirect mapping: "joinDomain"
        }
    }

    /**
     * It's overwritten because createLink use the request to recover the absolute path. And I prefer
     * to use "absolute:true" because it uses the grails.serverURL property
     * @return
     */
    @Override
    protected String generateLink(String action, linkParams) {
        createLink(absolute: true,
                controller: 'register', action: action,
                params: linkParams)
    }

    protected String generateLinkWithMapping(String mapping, linkParams) {
        createLink(absolute: true,
                mapping: mapping,
                params: linkParams)
    }


    /// FUNNEL TO COMPLETE THE PROFILE
    def campaignFunnelRegisterStart() {
        def model = index()
        model.namePlaceholderOverwritten = (g.message(code: 'register.funnel.association.name')).toString()
        model.emailPlaceholderOverwritten = (g.message(code: 'register.funnel.association.email')).toString()
        render view: 'index', model: model
    }

    def saveCampaignFunnelRegisterStart(KuorumRegisterCommand command) {
        registerUser(command, "funnelFillBasicData", [campaignId: params.campaignId])
    }

    private void qrDisabledRedirect() {
        log.warn("Try to use qr validation on a don't enable domain")
        flash.error = message code: 'landingPage.join.enabled.error'
        redirect mapping: "home", params: [evictCache: "true"]
    }
}

@Validateable
class KuorumRegisterCommand {

    def grailsApplication

    String email
    String name
    String password
    Boolean conditions
    String redirectUrl
    String ownerId
    String externalId
    String campaignId

    String getUsername() { email }// RegisterController.passwordValidator uses username
    static constraints = {
        name nullable: false, maxSize: 15
        email nullable: false, email: true, validator: { val, obj ->

            Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
            RegisterService registerService = (RegisterService) appContext.registerService
            obj.email = val ? val.toLowerCase() : ""
            String codeError = registerService.checkValidEmail(val)
            if (codeError) {
                return codeError
            }
        }
        password nullable: false
        conditions nullable: false
        redirectUrl nullable: true
        ownerId nullable: true
        externalId nullable: true
        campaignId nullable: true
//      validator: RegisterController.passwordValidator
    }
}

@Validateable
class ResendVerificationMailCommand {

    String email
    static constraints = {
        email nullable: false, email: true
    }
}


@Validateable
class CodeJoinCommand {

    String qrCode

    static constraints = {
        qrCode maxSize: 6, minSize: 6, nullable: false
    }
}

@Validateable
class ExternIdJoinCommand {

    String externalId
    String campaignId
    String ownerId
    Boolean conditions

    static constraints = {
        externalId nullable: false
        campaignId nullable: false
        ownerId nullable: false
        conditions nullable: false
    }
}