package kuorum.politician

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUserService
import kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand
import kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand
import kuorum.web.commands.profile.DomainUserPhoneValidationCommand
import kuorum.web.commands.profile.DomainValidationCommand
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.CensusLoginRDTO
import org.kuorum.rest.model.communication.*
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserExtraDataRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.UserPhoneValidationRDTO
import org.kuorum.rest.model.kuorumUser.validation.UserValidationRSDTO
import payment.campaign.*
import payment.contact.CensusService
import springSecurity.KuorumRegisterCommand

class CampaignValidationController {
    KuorumUserService kuorumUserService
    CookieUUIDService cookieUUIDService
    CampaignService campaignService
    SpringSecurityService springSecurityService
    CensusService censusService
    RegisterService registerService


    def step0RegisterWithCensusCode(){
        String censusLogin = params['censusLogin'];
        CensusLoginRDTO censusLoginData = censusService.getContactByCensusCode(censusLogin)
        if (censusLoginData == null){
            censusService.deleteCensusCode(censusLogin)
            log.info("Receviced an invalid censusLogin [${censusLogin}]")
            // Sending to campaign when the code is invalid.
            String censusRedirect = getCensusRedirect(true)
            render view: '/campaignValidation/step0RegisterWithCensusCode_ERROR' , model:[redirectUrl:censusRedirect]
        }else{
            CampaignRSDTO campaign = censusLoginData.getCampaign();
            ContactRSDTO contact = censusLoginData.getContact();
            logoutIfContactDifferentAsLoggedUser(contact);
            log.info("Receviced a valid censusLogin [${censusLogin}] -> Contact: ${contact.email}")
            if (campaign.closed){
                log.info("Receviced a valid censusLogin [${censusLogin}] -> Campaign is closed.");
                render view: '/campaignValidation/step0RegisterWithCensusCode_NOT_ACTIVE' , model:[redirectUrl:null, contact: contact, campaign:campaign]
            } else if (springSecurityService.isLoggedIn()){
//                flash.message="You are already logged"
                log.info("Receviced a valid censusLogin [${censusLogin}] -> User is the same as the censusLogin.");
                KuorumUserRSDTO userFromContact = censusService.createUserByCensusCode(censusLogin); // THis method creates the user if not exists and then validates it.
                censusService.deleteCensusCode(censusLogin)
                redirect uri:calcNextStepMappingName(campaign)
            }else if (contact.getMongoId()){
                // If user already exists, instead of create he will be validated
                KuorumUserRSDTO userFromContact = censusService.createUserByCensusCode(censusLogin);
                springSecurityService.reauthenticate userFromContact.getEmail()
                redirect uri:calcNextStepMappingName(campaign)
            }else{
                // DEFAULT
                render view: '/campaignValidation/step0RegisterWithCensusCode', model:[
                        contact: contact,
                        censusLogin:censusLogin,
                        campaign: campaign,
                        command:new KuorumRegisterCommand()]
            }
        }
    }

    private void logoutIfContactDifferentAsLoggedUser(ContactRSDTO contact){
        if (springSecurityService.isLoggedIn()){
            KuorumUserSession userSession = springSecurityService.principal
            if (userSession.email != contact.email){
                log.info("Logging out user (${userSession.email}) because it is using a censusLogin of different contact (${contact.email}")
                registerService.logout(request, response)
            }
        }
    }

    def step0RegisterWithCensusCodeSave(KuorumRegisterCommand command){

        String censusLogin = params['censusLogin'];
        CensusLoginRDTO censusLoginRDTO = censusService.getContactByCensusCode(censusLogin)

        if (!censusLoginRDTO){
            log.warn("Invalid census login ${params['censusLogin']}. Redirecting to home")
            flash.error="Your link is not valid. "
            redirect mapping:'home'
        }else{
            log.info("Creating user with ${params['censusLogin']}")
            KuorumUserRSDTO userRSDTO = censusService.createUserByCensusCode(censusLogin);
            springSecurityService.reauthenticate userRSDTO.email
            redirect uri:calcNextStepMappingName(censusLoginRDTO.getCampaign())
        }
    }


    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationCensus(){
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        [command: new DomainValidationCommand(), campaign: campaign]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationCensusSave(DomainValidationCommand command){
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()){
            render view: "stepCampaignValidationCensus", model:[command:command,campaign: campaign]
            return
        }
        KuorumUserSession user =  springSecurityService.principal
        UserValidationRSDTO validationRSDTO = kuorumUserService.userDomainValidation(user, campaign.getId(), command.ndi, command.postalCode, command.birthDate)
        if (validationRSDTO.censusStatus.isGranted()){
            redirect uri:calcNextStepMappingName(campaign,validationRSDTO)
        }else{
            flash.error =g.message(code:'kuorum.web.commands.profile.DomainValidationCommand.validationError')
            render view: "stepCampaignValidationCensus", model:[command:command, campaign: campaign]
        }
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationCustomCode(){
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        [command: new DomainUserCustomCodeValidationCommand(), campaign:campaign]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationCustomCodeSave(DomainUserCustomCodeValidationCommand command){
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()){
            render view: "stepCampaignValidationCustomCode", model:[command:command,campaign:campaign]
            return
        }
        KuorumUserSession userSession =  springSecurityService.principal
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.userCodeDomainValidation(userSession, campaign.getId(), command.customCode)
        String msg;
        if (userValidationRSDTO.codeStatus.isGranted()){
            msg = "Success validation"
        }else{
            msg = g.message(code:"kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.customCode.validationError", args:[userSession.email.replaceFirst(/([^@]{3}).*@(..).*/,"\$1***@\$2***")])
            log.info("Error validating user: ${msg}")
        }

        if (userValidationRSDTO.codeStatus.isGranted()){
            redirect uri:calcNextStepMappingName(campaign,userValidationRSDTO)
        }else{
            command.errors.rejectValue('customCode',"validationError", msg)
            render view: "stepCampaignValidationCustomCode", model:[command:command, campaign:campaign]
        }
    }


    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationPhoneNumber(){
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        return modelInputPhoneValidationStep(null, campaign)
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationPhoneCode(DomainUserPhoneValidationCommand command){
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()){
            render view: "stepCampaignValidationPhoneNumber", model:modelInputPhoneValidationStep(command, campaign)
            return
        }
        KuorumUserSession userSession =  springSecurityService.principal
        try{
            UserPhoneValidationRDTO userPhoneValidationRDTO = kuorumUserService.sendSMSWithValidationCode(userSession,campaign.getId(), command.phoneNumber.toString(), command.phoneNumberPrefix)
            [command:new DomainUserPhoneCodeValidationCommand(
                    phoneHash: userPhoneValidationRDTO.getHash(),
                    validationPhoneNumberPrefix: userPhoneValidationRDTO.getPhoneNumberPrefix(),
                    validationPhoneNumber: userPhoneValidationRDTO.getPhoneNumber()),
            campaign: campaign]
        }catch(KuorumException e){
            command.errors.rejectValue("phoneNumber", 'kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumber.repeatedNumber')
            render view: "stepCampaignValidationPhoneNumber", model:[command:command]
            return
        }catch(Exception e){
            flash.message="Internal error. Try again or contact with info@kuorum.org"
            render view: "stepCampaignValidationPhoneNumber", model:[command:command]
            return
        }
    }

    private def modelInputPhoneValidationStep(DomainUserPhoneValidationCommand command, CampaignRSDTO campaign){
        if (command == null){
            command = new DomainUserPhoneValidationCommand()
        }
        KuorumUserSession userSession =  springSecurityService.principal
        KuorumUserExtraDataRSDTO extraDataRSDTO = kuorumUserService.findUserExtendedDataRSDTO(userSession)
        String phone = extraDataRSDTO.phoneNumber?.encodeAsHiddenPhone()
        Boolean predefinedPhone = extraDataRSDTO.phoneNumber?true:false;
        return [predefinedPhone:predefinedPhone, phone:phone, command: command, campaign: campaign]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationPhoneCodeSave(DomainUserPhoneCodeValidationCommand command){
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()){
            render view: "stepCampaignValidationPhoneCode", model:[command:command]
            return
        }
        KuorumUserSession userSession = springSecurityService.principal
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.userPhoneDomainValidation(userSession, campaign.getId(),command.validationPhoneNumberPrefix, command.validationPhoneNumber, command.phoneHash, command.phoneCode)
        if (userValidationRSDTO.phoneStatus.isGranted()){
            redirect uri:calcNextStepMappingName(campaign,userValidationRSDTO)
        }else{
            command.errors.rejectValue("phoneCode","kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.validationError");
            render view: "stepCampaignValidationPhoneCode", model:[command:command, campaign: campaign]
        }
    }


    private String calcNextStepMappingName(CampaignRSDTO campaignRSDTO, UserValidationRSDTO validationRSDTO=null) {
        if (springSecurityService.isLoggedIn()) {
            KuorumUserSession userSession = springSecurityService.principal
            if (!validationRSDTO){
                validationRSDTO = kuorumUserService.getUserValidationStatus(userSession, campaignRSDTO.getId())
            }
            if (!validationRSDTO.censusStatus.granted) return g.createLink(mapping: "campaignValidationCensus", params: campaignRSDTO.encodeAsLinkProperties());
            else if (!validationRSDTO.codeStatus.granted) return g.createLink(mapping: "campaignValidationCode",params: campaignRSDTO.encodeAsLinkProperties());
            else if (!validationRSDTO.phoneStatus.granted) return g.createLink(mapping: "campaignValidationPhoneNumber", params: campaignRSDTO.encodeAsLinkProperties())
            else if (campaignRSDTO){
                return g.createLink(mapping: "campaignShow", params: campaignRSDTO.encodeAsLinkProperties())
            }else {
                return getCensusRedirect(true);
            }
        } else {
            // NO LOGGED
            String censusRedirect = getCensusRedirect(true);
            if (campaignRSDTO){
                return g.createLink(mapping: "campaignShow", params: campaignRSDTO.encodeAsLinkProperties())
            } else {
                return censusRedirect;
            }
        }
    }

    private CampaignRSDTO getCampaignRSDTO(def params){
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        CampaignRSDTO campaignRSDTO = campaignService.find(user, Long.parseLong(params.campaignId))
        if (!campaignRSDTO) {
            throw new KuorumException(message(code: "post.notFound") as String)
        }
        return campaignRSDTO;
    }

    private String getCensusRedirect(Boolean deleteCookie){
        String censusRedirect = cookieUUIDService.getDomainCookie(WebConstants.COOKIE_URL_CALLBACK_CENSUS_LOGIN)
        if (deleteCookie){
            cookieUUIDService.deleteDomainCookie(WebConstants.COOKIE_URL_CALLBACK_CENSUS_LOGIN)
        }
        if (!censusRedirect){
            censusRedirect = g.createLink(mapping: "home");
        }
        return censusRedirect
    }
}
