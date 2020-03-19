package kuorum

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.core.model.UserType
import kuorum.dashboard.DashboardService
import kuorum.notifications.NotificationService
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.PoliticianService
import kuorum.web.commands.customRegister.Step2Command
import kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand
import kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand
import kuorum.web.commands.profile.DomainUserPhoneValidationCommand
import kuorum.web.commands.profile.DomainValidationCommand
import org.springframework.web.servlet.LocaleResolver

class CustomRegisterController {

    def springSecurityService
    KuorumUserService kuorumUserService
    DashboardService dashboardService
    NotificationService notificationService
    LocaleResolver localeResolver
    RegisterService registerService
    PoliticianService politicianService

    def afterInterceptor = {}

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step2(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        String recommendedAlias = kuorumUserService.generateValidAlias(user.name, true)
        [command:new Step2Command(user,recommendedAlias)]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step2Save(Step2Command  command){
        KuorumUser user =  KuorumUser.get(springSecurityService.principal.id)
        if (command.hasErrors()){
            render view: "step2", model:[command:command]
            return
        }
        kuorumUserService.updateAlias(user, command.alias)

        user.personalData = user.personalData?:new kuorum.users.PersonalData()
        user.userType = UserType.PERSON
        user.personalData.provinceCode = command.homeRegion.iso3166
        user.language = command.language
        user.personalData.gender = command.gender

//        user.password = registerService.encodePassword(user, command.password)
        kuorumUserService.updateUser(user)
        redirect mapping:calcNextStepMappingName()
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidation(){
        [command: new DomainValidationCommand()]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationSave(DomainValidationCommand command){
        if (command.hasErrors()){
            render view: "stepDomainValidation", model:[command:command]
            return
        }
        KuorumUserSession user =  springSecurityService.principal
        Boolean isValid = kuorumUserService.userDomainValidation(user, command.ndi, command.postalCode, command.birthDate)
        user =  springSecurityService.principal
        if (user.censusValid){
            redirect mapping:calcNextStepMappingName()
        }else{
            flash.error =g.message(code:'kuorum.web.commands.profile.DomainValidationCommand.validationError')
            render view: "stepDomainValidation", model:[command:command]
        }
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationCustomCode(){
        [command: new DomainUserCustomCodeValidationCommand()]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationCustomCodeSave(DomainUserCustomCodeValidationCommand command){
        if (command.hasErrors()){
            render view: "stepDomainValidationCustomCode", model:[command:command]
            return
        }
        KuorumUserSession user =  springSecurityService.principal
        kuorumUserService.userCodeDomainValidation(user, command.customCode)
        user =  springSecurityService.principal
        if (user.codeValid){
            redirect mapping:calcNextStepMappingName()
        }else{
            command.errors.rejectValue('customCode','kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.customCode.validationError')
            render view: "stepDomainValidationCustomCode", model:[command:command]
        }
    }


    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationPhoneNumber(){
        [command: new DomainUserPhoneValidationCommand()]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationPhoneCode(DomainUserPhoneValidationCommand command){
        if (command.hasErrors()){
            render view: "stepDomainValidationPhoneNumber", model:[command:command]
            return
        }
        KuorumUserSession userSession =  springSecurityService.principal
        try{
            String hash = kuorumUserService.sendSMSWithValidationCode(userSession, command.completePhoneNumber)
            [command:new DomainUserPhoneCodeValidationCommand(phoneHash: hash)]
        }catch(KuorumException e){
            command.errors.rejectValue("phoneNumber", 'kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumber.repeatedNumber')
            render view: "stepDomainValidationPhoneNumber", model:[command:command]
            return
        }catch(Exception e){
            flash.message="Internal error. Try again or contact with info@kuorum.org"
            render view: "stepDomainValidationPhoneNumber", model:[command:command]
            return
        }
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationPhoneCodeSave(DomainUserPhoneCodeValidationCommand command){
        if (command.hasErrors()){
            render view: "stepDomainValidationPhoneCode", model:[command:command]
            return
        }
        KuorumUserSession userSession = springSecurityService.principal
        Boolean isValid = kuorumUserService.userPhoneDomainValidation(userSession, command.phoneHash, command.phoneCode)
        userSession =  springSecurityService.principal
        if (userSession.phoneValid){
            redirect mapping:calcNextStepMappingName()
        }else{
            command.errors.rejectValue("phoneCode","kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.validationError");
            render view: "stepDomainValidationPhoneCode", model:[command:command]
        }
    }



    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step3(){
        [:]
    }

    private String calcNextStepMappingName(){
//        KuorumUserSession userSession = springSecurityService.principal
//        if (!userSession.censusValid) return "customProcessRegisterDomainValidation"
//        if (!userSession.codeValid) return "customProcessRegisterCustomCodeValidation"
//        if (!userSession.phoneValid) return "customProcessRegisterPoneValidationNumber"
        return "customProcessRegisterStep3";
    }
}
