package kuorum

import grails.converters.JSON
import grails.plugin.cookie.CookieService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.core.model.UserType
import kuorum.dashboard.DashboardService
import kuorum.notifications.NotificationService
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.PoliticianService
import kuorum.web.commands.customRegister.Step2Command
import kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand
import kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand
import kuorum.web.commands.profile.DomainUserPhoneValidationCommand
import kuorum.web.commands.profile.DomainValidationCommand
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserExtraDataRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.UserPhoneValidationRDTO
import org.springframework.web.servlet.LocaleResolver
import payment.contact.CensusService
import springSecurity.KuorumRegisterCommand

class CustomRegisterController {

    def springSecurityService
    KuorumUserService kuorumUserService
    CookieUUIDService cookieUUIDService
    CensusService censusService;

    def afterInterceptor = {}

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step2(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        String recommendedAlias = kuorumUserService.generateValidAlias(user.name, true)
        [command:new Step2Command(user,recommendedAlias)]
    }


    def step0RegisterWithCensusCode(){
        String censusLogin = params['censusLogin'];
        ContactRSDTO contact = censusService.getContactByCensusCode(censusLogin)
        if (contact == null){
            flash.message=g.message(code:'kuorum.web.commands.profile.directCensusLogin.wrongCode')
            censusService.deleteCensusCode(censusLogin)
            redirect uri:calcNextStepMappingName()
        }else{
            log.info("Receviced a valid censusLogin [${censusLogin}] -> Contact: ${contact.email}")
            BasicDataKuorumUserRSDTO userFromContact;
            if (contact.getMongoId()){
                userFromContact = kuorumUserService.findBasicUserRSDTO(contact.getMongoId(), true)
                censusService.deleteCensusCode(censusLogin)
                springSecurityService.reauthenticate userFromContact.getEmail()
                redirect uri:calcNextStepMappingName()
            }else{
                // DEFAULT
                render view: '/customRegister/step0RegisterWithCensusCode', model:[
                        contact: contact,
                        censusLogin:censusLogin,
                        command:new KuorumRegisterCommand()]
            }
        }
    }

    def step0RegisterWithCensusCodeSave(KuorumRegisterCommand command){

        String censusLogin = params['censusLogin'];
        ContactRSDTO contact = censusService.getContactByCensusCode(censusLogin)
        
        if (!contact){
            log.warn("Invalid census login ${params['censusLogin']}. Redirecting to home")
            redirect mapping:'home'
        }else{
            log.info("Creating user with ${params['censusLogin']}")
            KuorumUserRSDTO userRSDTO = censusService.createUserByCensusCode(censusLogin);
            springSecurityService.reauthenticate userRSDTO.email
            redirect uri:calcNextStepMappingName()
        }
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
        redirect uri:calcNextStepMappingName()
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
            redirect uri:calcNextStepMappingName()
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
        KuorumUserSession userSession =  springSecurityService.principal
        Boolean isValidated;
        String msg;
        try{
            isValidated = kuorumUserService.userCodeDomainValidation(userSession, command.customCode)
            msg = "Success validation"
        }catch(KuorumException e){
//            msg = g.message(code:e.errors[0].code, args:[userSession.email.replaceFirst(/([^@]{3}).*@(..).*/,"\$1***@\$2***")])
            msg = e.errors[0].code
            log.info("Error validating user: ${msg}")
        }catch(Exception e){
            msg = "Internal error: ${e.getMessage()}"
        }
        userSession =  springSecurityService.principal
        if (userSession.codeValid){
            redirect uri:calcNextStepMappingName()
        }else{
            command.errors.rejectValue('customCode',msg)
            render view: "stepDomainValidationCustomCode", model:[command:command]
        }
    }


    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationPhoneNumber(){
        return modelInputPhoneValidationStep(null)
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationPhoneCode(DomainUserPhoneValidationCommand command){
        if (command.hasErrors()){
            render view: "stepDomainValidationPhoneNumber", model:modelInputPhoneValidationStep(command)
            return
        }
        KuorumUserSession userSession =  springSecurityService.principal
        try{
            UserPhoneValidationRDTO userPhoneValidationRDTO = kuorumUserService.sendSMSWithValidationCode(userSession, command.phoneNumber.toString(), command.phoneNumberPrefix)
            [command:new DomainUserPhoneCodeValidationCommand(
                    phoneHash: userPhoneValidationRDTO.getHash(),
                    validationPhoneNumberPrefix: userPhoneValidationRDTO.getPhoneNumberPrefix(),
                    validationPhoneNumber: userPhoneValidationRDTO.getPhoneNumber())]
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

    private def modelInputPhoneValidationStep(DomainUserPhoneValidationCommand command){
        if (command == null){
            command = new DomainUserPhoneValidationCommand()
        }
        KuorumUserSession userSession =  springSecurityService.principal
        KuorumUserExtraDataRSDTO extraDataRSDTO = kuorumUserService.findUserExtendedDataRSDTO(userSession)
        String phone = extraDataRSDTO.phoneNumber?.replaceFirst(/^.*(\d{4})$/,"******\$1");
        Boolean predefinedPhone = extraDataRSDTO.phoneNumber?true:false;
        return [predefinedPhone:predefinedPhone, phone:phone, command: command]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepDomainValidationPhoneCodeSave(DomainUserPhoneCodeValidationCommand command){
        if (command.hasErrors()){
            render view: "stepDomainValidationPhoneCode", model:[command:command]
            return
        }
        KuorumUserSession userSession = springSecurityService.principal
        Boolean isValid = kuorumUserService.userPhoneDomainValidation(userSession, command.validationPhoneNumberPrefix, command.validationPhoneNumber, command.phoneHash, command.phoneCode)
        userSession =  springSecurityService.principal
        if (userSession.phoneValid){
            redirect uri:calcNextStepMappingName()
        }else{
            command.errors.rejectValue("phoneCode","kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.validationError");
            render view: "stepDomainValidationPhoneCode", model:[command:command]
        }
    }



    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def step3(){
        [:]
    }

    private String calcNextStepMappingName() {
        String censusRedirect = cookieUUIDService.getDomainCookie(WebConstants.COOKIE_URL_CALLBACK_CENSUS_LOGIN)
        if (springSecurityService.isLoggedIn()) {
            KuorumUserSession userSession = springSecurityService.principal
            if (!userSession.censusValid) return g.createLink(mapping: "customProcessRegisterDomainValidation");
            else if (!userSession.codeValid) return g.createLink(mapping: "customProcessRegisterCustomCodeValidation")
            else if (!userSession.phoneValid) return g.createLink(mapping: "customProcessRegisterPoneValidationNumber")
            else if (censusRedirect) {
                cookieUUIDService.deleteDomainCookie(WebConstants.COOKIE_URL_CALLBACK_CENSUS_LOGIN)
                return censusRedirect;
            }
//        return g.createLink(mapping:"customProcessRegisterStep3")
            return g.createLink(mapping: "home")
        } else {
            // NO LOGGED
            if (censusRedirect) {
                cookieUUIDService.deleteDomainCookie(WebConstants.COOKIE_URL_CALLBACK_CENSUS_LOGIN)
                return censusRedirect;
            }
            //        return g.createLink(mapping:"customProcessRegisterStep3")
            return g.createLink(mapping: "home")
        }
    }
}
