package springSecurity

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.plugin.springsecurity.ui.ResetPasswordCommand
import grails.plugin.springsecurity.ui.SpringSecurityUiService
import grails.validation.Validateable
import groovyx.net.http.RESTClient
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.files.FileService
import kuorum.notifications.NotificationService
import kuorum.register.IOAuthService
import kuorum.register.RegisterService
import kuorum.solr.IndexSolrService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.customRegister.ForgotUserPasswordCommand
import kuorum.web.users.KuorumRegistrationCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.RememberMeServices

class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {

    def kuorumMailService
    RegisterService registerService

    KuorumUserService kuorumUserService
    SpringSecurityService springSecurityService
    SpringSecurityUiService springSecurityUiService
    NotificationService notificationService
    IndexSolrService indexSolrService

    FileService fileService
    CookieUUIDService cookieUUIDService

    @Value('${recaptcha.providers.google.secretKey}')
    String RECAPTCHA_SECRET

    def index() {
        def copy = [:] + (flash.chainedParams ?: [:])
        copy.remove 'controller'
        copy.remove 'action'
        def config = SpringSecurityUtils.securityConfig

        if (springSecurityService.isLoggedIn()) {
            redirect uri: config.successHandler.defaultTargetUrl
//            flash.message = message(code:'register.alreadyRegistered')
            return
        }
        [command: new KuorumRegisterCommand(copy)]
    }

    def register(KuorumRegisterCommand command) {
        if (command.hasErrors()) {
            render view: 'index', model: [command: command]
            return
        }

        if(!verifyRegister()){
            render ([success:false] as JSON)
            return
        }

        KuorumUser user = registerService.registerUser(command)
        redirect mapping:"customProcessRegisterStep2"
    }

    def verifyRegister(){
        String secretKey = RECAPTCHA_SECRET
        String responseCaptcha = params.'g-recaptcha-response'
        String path =  "/recaptcha/api/siteverify"
        def query = [secret:secretKey, response:responseCaptcha]
        RESTClient mailKuorumServices = new RESTClient( "https://www.google.com")
        def response = mailKuorumServices.get(
                path: path,
                headers: ["User-Agent": "Kuorum Web"],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )

        if(!response.data.success)
            return false
        else
            return true
    }

    RememberMeServices rememberMeServices

    def registerRRSSOAuthAjax(){
        params.remove("controller")
        params.remove("action")
        params.remove("language")
        String providerName = params.remove("provider")
        String redirectAdminConfig= params.remove("redirectAdminConfig") // This params is used from kuorum.org to redirect first time to domain configuration
        IOAuthService providerService = grailsApplication.mainContext.getBean("${providerName}OAuthService")
        org.scribe.model.Token token = providerService.createTokenFromAjaxParams(params)
        if (!token){
            flash.message = "This code is already used or not exists"
            redirect mapping:'home'
            return
        }
        grails.plugin.springsecurity.oauth.OAuthToken oAuthToken = providerService.createAuthToken(token)
        oAuthToken.authenticated = true
        SecurityContextHolder.context.authentication = oAuthToken
        rememberMeServices.loginSuccess(request, response, springSecurityService.authentication)
        if (oAuthToken.newUser){
            try{
                indexSolrService.deltaIndex()
            }catch (Exception e){
                log.error("Error recovering and indexing new user. Reindex manually")
            }
        }
        if (redirectAdminConfig){
            redirect mapping: 'adminDomainRegisterStep1'
        }else{
            render ([result:"success"] as JSON)
        }
    }

    def ajaxRegister(KuorumRegisterCommand command){
        if (command.hasErrors()) {
            render ([success:false, error: g.message(error: command.errors.getAllErrors().first())] as JSON)
            return
        }

        if(!verifyRegister()){
            render ([success:false, error:'You looks like a robot'] as JSON)
            return
        }

        KuorumUser user = registerService.registerUser(command)
//        String automaticAlias = kuorumUserService.generateValidAlias(user.name)
//        kuorumUserService.updateAlias(user, automaticAlias)
//        kuorumUserService.updateUser(user)
        if (user){
            render ([success:true] as JSON)
        }else{
            render ([success:false] as JSON)
        }
    }

    def checkEmail(String email){
        KuorumUser user = KuorumUser.findByEmailAndDomain(email, CustomDomainResolver.domain)
        if (user){
            render Boolean.FALSE.toString()
        }else{
            render Boolean.TRUE.toString()
        }
    }

    def registerSuccess(){}

    def resendRegisterVerification(){
        [command:new ResendVerificationMailCommand(email: params.email)]
    }

    def resendVerification(ResendVerificationMailCommand command){
        KuorumUser user = KuorumUser.findByEmailAndDomain(command.email.toLowerCase(),CustomDomainResolver.domain)
        if (!user){
            flash.error=message(code:'springSecurity.ResendVerificationMailCommand.email.notUserExists')
            command.errors.rejectValue('email', 'springSecurity.ResendVerificationMailCommand.email.notUserExists')
        }
        KuorumRegistrationCode registrationCode = KuorumRegistrationCode.findByUsername(user.email)
        if (!registrationCode){
            flash.error=message(code:'springSecurity.ResendVerificationMailCommand.email.notUserExists')
            command.errors.rejectValue('email', 'springSecurity.ResendVerificationMailCommand.email.notToken')
        }
        if (command.hasErrors()){
            render view: 'resendRegisterVerification', model:[command:command]
            return
        }

        log.info("Usuario $user.name recordando token  $registrationCode.token")
        String url = generateLinkWithMapping('registerVerifyAccount', [t: registrationCode.token])

        kuorumMailService.sendRegisterUser(user,url)
        flash.chainedParams = [link:url]
        flash.message=message(code:'register.locked.resendSuccess')
        redirect mapping:"registerSuccess"
    }



    def sendConfirmationEmail(){
        KuorumUser user = KuorumUser.findById(springSecurityService.principal.id)
        String url=createLink(absolute: true, mapping:'registerVerifyAccount', params: [t: params.t])
        kuorumMailService.sendRegisterUser(user,url)
        redirect mapping:'home'
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
        notificationService.sendWelcomeRegister(user)
        if (registerService.isPasswordSetByUser(user)){
            redirect mapping:'dashboard'
        }else{
            redirect mapping:'customProcessRegisterStep2', params: [tour:true]
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

    def forgotPasswordForm(){
        render template: "/layouts/recoverPassword", model:[ modalId:'registro']
    }

    def ajaxValidationForgotPassword(ForgotUserPasswordCommand command) {

        if (command.hasErrors()) {
            render Boolean.FALSE.toString()
        }else{
            render Boolean.TRUE.toString()
        }
    }

    def forgotPassword(){
        [command: new ForgotUserPasswordCommand()]
    }
    def forgotPasswordPost = { ForgotUserPasswordCommand command ->

        if (command.hasErrors()) {
            render view: "forgotPassword", model:[command:command]
            return
        }

        KuorumUser user =  KuorumUser.findByEmailAndDomain(command.email, CustomDomainResolver.domain)
        if (!user){
            command.errors.rejectValue("email", "kuorum.web.commands.customRegister.ForgotUserPasswordCommand.email.invalid")
            render view: "forgotPassword", model:[command:command]
            return
        }

        def registrationCode = new RegistrationCode(username: command.email)
        registrationCode.save(flush: true)

        String url = generateLinkWithMapping('resetPasswordChange', [t: registrationCode.token])

        kuorumMailService.sendResetPasswordMail(user, url)

        redirect mapping:"resetPasswordSent"
    }

    def forgotPasswordSuccess = {
    }

    def resetPassword(ResetPasswordCommand command) {

        String token = params.t

        def registrationCode = token ? RegistrationCode.findByToken(token) : null
        if (!registrationCode) {
            flash.error = message(code: 'spring.security.ui.resetPassword.badCode')
            redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
            return
        }

        String ulrCallback = cookieUUIDService.getRememberPasswordRedirect()
        String urlCancelResetPassword = g.createLink(mapping:"login")
        if (!ulrCallback){
            def conf = SpringSecurityUtils.securityConfig
            ulrCallback = conf.ui.register.postResetUrl ?: conf.successHandler.defaultTargetUrl
        }else{
            urlCancelResetPassword = ulrCallback
        }
        if (!request.post) {
            //Request change password via GET [Not form filled]
            return [token: token, command: new ResetPasswordCommand(), urlCancelResetPassword:urlCancelResetPassword]
        }

        command.username = registrationCode.username
        command.validate()

        if (command.hasErrors()) {
            return [token: token, command: command, urlCancelResetPassword:urlCancelResetPassword]
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
}

@Validateable
class KuorumRegisterCommand{

    def grailsApplication

    String email
    String name
    String password
    Boolean conditions
    String redirectUrl

    String getUsername(){ email }// RegisterController.passwordValidator uses username
    static constraints = {
        name nullable: false, maxSize: 15
        email nullable:false, email:true, validator: { val, obj ->
            if (val && KuorumUser.findByEmailAndDomain(val.toLowerCase(), CustomDomainResolver.domain)) {
                obj.email = val.toLowerCase()
                return 'registerCommand.username.unique'
            }
            def mailParts = val.split("@")
            if (mailParts.size() == 2){
                def domain = mailParts[1]
                def notAllowed = obj.grailsApplication.config.kuorum.register.notAllowedTemporalDomainEmails.find{"@$domain".equalsIgnoreCase(it)}
                if (notAllowed){
                    return 'registerCommand.username.notAllowed'
                }
            }
        }
        password nullable:false
        conditions nullable: false
        redirectUrl nullable:true
//      validator: RegisterController.passwordValidator
    }
}

@Validateable
class ResendVerificationMailCommand{

    String email
    static constraints = {
        email nullable:false, email:true
    }
}


