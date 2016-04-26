package springSecurity

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.plugin.springsecurity.ui.ResetPasswordCommand
import grails.validation.Validateable
import kuorum.KuorumFile
import kuorum.files.FileService
import kuorum.mail.MailchimpService
import kuorum.notifications.NotificationService
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.customRegister.ContactRegister
import kuorum.web.commands.customRegister.ForgotUserPasswordCommand
import org.springframework.context.i18n.LocaleContextHolder

class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {

    def kuorumMailService
    RegisterService registerService

    KuorumUserService kuorumUserService

    NotificationService notificationService

    FileService fileService
    MailchimpService mailchimpService

    def index() {
        def copy = [:] + (flash.chainedParams ?: [:])
        copy.remove 'controller'
        copy.remove 'action'
        def config = SpringSecurityUtils.securityConfig

        if (springSecurityService.isLoggedIn()) {
            redirect uri: config.successHandler.defaultTargetUrl
            flash.message = message(code:'register.alreadyRegistered')
            return
        }
        [command: new KuorumRegisterCommand(copy)]
    }

    def register(KuorumRegisterCommand command) {
        if (command.hasErrors()) {
            render view: 'index', model: [command: command]
            return
        }

        KuorumUser user = registerService.registerUser(command)
        if (params.editor){
            //If the registrations comes from editorLandingPage
            notificationService.sendEditorPurchaseNotification(user)
        }
        redirect mapping:"home"
    }

    def contactRegister(ContactRegister contactRegister){
        if (!contactRegister.validate()){
            flash.message = g.message(code: 'register.contactRegister.error.validation')
            if (contactRegister.politician){
                redirect mapping:"userShow", params: contactRegister.politician.encodeAsLinkProperties()
            }else{
                redirect mapping:"politicians"
            }
            return;
        }

        if (springSecurityService.isLoggedIn()){
            KuorumUser user = springSecurityService.getCurrentUser();
            notificationService.sendPoliticianContactNotification(contactRegister.politician, user, contactRegister.message, contactRegister.cause)
            flash.message = g.message(code: 'register.contactRegister.success.userLogged', args: [contactRegister.politician.name])
            redirect mapping:"userShow", params: contactRegister.politician.encodeAsLinkProperties()
        }else{
            KuorumUser user = KuorumUser.findByEmail(contactRegister.email)
            if (user){
                //ERROR: User should be logged in
                flash.error = g.message(code: 'register.contactRegister.success.userNotLogged', args: [contactRegister.politician.name])
                redirect mapping:"secUserShow", params: contactRegister.politician.encodeAsLinkProperties()
            }else{
                //REGISTER USER
                user = registerService.registerUserContactingPolitician(contactRegister)
                flash.message = g.message(code: 'register.contactRegister.success.userJustRegisted', args: [contactRegister.politician.name], encodeAs: 'raw' )
                redirect mapping:"userShow", params: contactRegister.politician.encodeAsLinkProperties()
            }
        }
    }

    def registerSuccess(){}

    def resendRegisterVerification(){
        [command:new ResendVerificationMailCommand(email: params.email)]
    }

    def resendVerification(ResendVerificationMailCommand command){
        KuorumUser user = KuorumUser.findByEmail(command.email.toLowerCase())
        if (!user){
            flash.error=message(code:'springSecurity.ResendVerificationMailCommand.email.notUserExists')
            command.errors.rejectValue('email', 'springSecurity.ResendVerificationMailCommand.email.notUserExists')
        }
        RegistrationCode registrationCode = RegistrationCode.findByUsername(user.email)
        if (!registrationCode){
            flash.error=message(code:'springSecurity.ResendVerificationMailCommand.email.notUserExists')
            command.errors.rejectValue('email', 'springSecurity.ResendVerificationMailCommand.email.notToken')
        }
        if (command.hasErrors()){
            render view: 'resendRegisterVerification', model:[command:command]
            return
        }

        log.info("Usuario $user.name recordando token  $registrationCode.token")
        String url = generateLink('verifyRegistration', [t: registrationCode.token])

        kuorumMailService.sendRegisterUser(user,url)
        flash.chainedParams = [link:url]
        flash.message=message(code:'register.locked.resendSuccess')
        redirect mapping:"registerSuccess"
    }



    def sendConfirmationEmail(){
        KuorumUser user = KuorumUser.findById(springSecurityService.principal.id)
        String url=createLink(absolute: true, controller: 'register', action: 'verifyRegistration', params: [t: params.t])
        kuorumMailService.sendRegisterUser(user,url)
        redirect mapping:'home'
    }


    def verifyRegistration() {

        def conf = SpringSecurityUtils.securityConfig
        String defaultTargetUrl = conf.successHandler.defaultTargetUrl

        String token = params.t

        RegistrationCode registrationCode = token ? RegistrationCode.findByToken(token) : null
        if (!registrationCode) {
            flash.error = message(code: 'spring.security.ui.register.badCode')
            redirect uri: defaultTargetUrl
            return
        }

        KuorumUser user = registerService.createUserByRegistrationCode(registrationCode)

        if (!user) {
            flash.error = message(code: 'spring.security.ui.register.userNotFound')
            redirect uri: defaultTargetUrl
            return
        }
//        render view:'selectMyPassword', model:[userId:user.id, command:new ResetPasswordCommand()]
        redirect mapping:'tourStart'
    }


    def selectMyPassword (ResetPasswordCommand command){
        KuorumUser user = KuorumUser.findById(params.userId)
        if (!user){
            redirect mapping:'home'
            return;
        }
        def conf = SpringSecurityUtils.securityConfig
        String defaultTargetUrl = conf.successHandler.defaultTargetUrl
        if (!command.validate()){
            render view: 'selectMyPassword' , model:[userId:user.id, command:command]
        }else{
            user.password = registerService.encodePassword(user, command.password)

            if(user.validate()){
                def renderMap = registerService.save(user)
                if(renderMap.errorMsg){
                    flash.message = renderMap.errorMsg
                    flash.typeMessage = 'error'
                    redirect action: 'index'
                }else{
                    springSecurityService.reauthenticate user.email
                    flash.message = message(code: 'spring.security.ui.register.passwordComplete')
                    redirect uri: conf.ui.register.postRegisterUrl ?: defaultTargetUrl
                }
            }
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

        def registrationCode = new RegistrationCode(username: command.user.email)
        registrationCode.save(flush: true)

        String url = generateLinkWithMapping('resetPasswordChange', [t: registrationCode.token])

        kuorumMailService.sendResetPasswordMail(command.user, url)

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

        if (!request.post) {
            return [token: token, command: new ResetPasswordCommand()]
        }

        command.username = registrationCode.username
        command.validate()

        if (command.hasErrors()) {
            return [token: token, command: command]
        }

        String salt = saltSource instanceof NullSaltSource ? null : registrationCode.username
        RegistrationCode.withTransaction { status ->
            KuorumUser user = KuorumUser.findByEmail(registrationCode.username)
            user.accountLocked = false
            user.password = springSecurityUiService.encodePassword(command.password, salt)
            user.save()
            registrationCode.delete()
        }

        springSecurityService.reauthenticate registrationCode.username

        flash.message = message(code: 'spring.security.ui.resetPassword.success')

        def conf = SpringSecurityUtils.securityConfig
        String postResetUrl = conf.ui.register.postResetUrl ?: conf.successHandler.defaultTargetUrl
        redirect uri: postResetUrl
    }

    def downloadPressKit(ResendVerificationMailCommand command){
        if (command.hasErrors()){
            return "Not valid mail";
        }
        Locale locale = LocaleContextHolder.getLocale();
        mailchimpService.addPress(command.email.split("@")[0], command.email, locale)
        String preFileName = "KuorumCaseStudy"
        String year = "2016"
        String ext = "pdf"
        String langPressKit = "en"
        if (locale.getLanguage() == "es"){
            langPressKit = "es"
        }

        KuorumFile kuorumFile = new KuorumFile()
        String fileName = "${preFileName}_${year}_${langPressKit}.${ext}"
        kuorumFile.storagePath="static/press/${fileName}"
        InputStream fileData = fileService.readFile(kuorumFile)
        response.setContentType("application/zip")
        response.setHeader("Content-disposition", "attachment; filename=\"${fileName}\"")
        response.outputStream << fileData
        fileData.close()
        return
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

    @Override
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

    public String getUsername(){ email }// RegisterController.passwordValidator uses username
    static constraints = {
        importFrom KuorumUser, include:["name"]
        email nullable:false, email:true, validator: { val, obj ->
            if (val && KuorumUser.findByEmail(val.toLowerCase())) {
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
        password: nullable:true
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


