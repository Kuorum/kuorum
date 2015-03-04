package springSecurity

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.plugin.springsecurity.ui.ResetPasswordCommand
import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.users.RoleUser
import kuorum.web.commands.customRegister.ForgotUserPasswordCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.register.RegisterService

class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {

    def kuorumMailService
    RegisterService registerService


    def index() {
        def copy = [:] + (flash.chainedParams ?: [:])
        copy.remove 'controller'
        copy.remove 'action'
        [command: new KuorumRegisterCommand(copy)]
    }

    def register(KuorumRegisterCommand command) {
        if (command.hasErrors()) {
            render view: 'index', model: [command: command]
            return
        }

        KuorumUser user = registerService.registerUser(command)
        redirect mapping:"home"
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
        render view:'selectMyPassword', model:[userId:user.id]
    }


    def choosePassword (){
        KuorumUser user = KuorumUser.findById(params.userId)
        def conf = SpringSecurityUtils.securityConfig
        String defaultTargetUrl = conf.successHandler.defaultTargetUrl
        if (!(params.password1 == params.password2)){
            flash.message = message (code:'grails.plugin.springsecurity.ui.ResetPasswordCommand.password2.helpBlock')
            flash.typeMessage= 'error'
            render view: 'selectMyPassword' , model:[userId:user.id]
        }else{
            String salt = saltSource instanceof NullSaltSource ? null : user.name
            user.password = springSecurityUiService.encodePassword(params.password1, salt)

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
        importFrom EditUserProfileCommand, include:["name"]
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


