package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.UserType
import kuorum.dashboard.DashboardService
import kuorum.notifications.NotificationService
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.PoliticianService
import kuorum.web.commands.customRegister.Step2Command
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
        if (CustomDomainResolver.domainRSDTO?.validation){
            redirect mapping:"customProcessRegisterDomainValidation"
        }else{
            redirect mapping:"customProcessRegisterStep3"
        }
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
        if (user.censusValid){
            if (user.phoneValid){
                redirect mapping:"customProcessRegisterStep3"
            }else{
                redirect mapping:"customProcessRegisterPoneValidationNumber"
            }
        }else{
            flash.error =g.message(code:'kuorum.web.commands.profile.DomainValidationCommand.validationError')
            render view: "stepDomainValidation", model:[command:command]
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
        String hash = kuorumUserService.sendSMSWithValidationCode(userSession, command.completePhoneNumber)
        [command:new DomainUserPhoneCodeValidationCommand(phoneHash: hash)]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationPhoneCodeSave(DomainUserPhoneCodeValidationCommand command){
        if (command.hasErrors()){
            render view: "stepDomainValidationPhoneCode", model:[command:command]
            return
        }
        KuorumUserSession userSession = springSecurityService.principal
        Boolean isValid = kuorumUserService.userPhoneDomainValidation(userSession, command.phoneHash, command.phoneCode)

        if (userSession.phoneValid){
            redirect mapping:"customProcessRegisterStep3"
        }else{
            command.errors.rejectValue("phoneCode","kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.validationError");
            render view: "stepDomainValidationPhoneCode", model:[command:command]
        }
    }



    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step3(){
        [:]
    }
}
