package kuorum.politician

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.security.evidences.Evidences
import kuorum.security.evidences.HttpRequestRecoverEvidences
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


    def initValidation(){
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        redirect uri:calcNextStepMappingName(campaign)
    }

    def step0RegisterWithCensusCode(){
        String censusLogin = params['censusLogin'];
        CensusLoginRDTO censusLoginData = censusService.getContactByCensusCode(censusLogin)
        if (censusLoginData == null){
            censusService.deleteCensusCode(censusLogin)
            log.info("[censusLogion: ${censusLogin}] : Receviced an invalid census login")
            // Sending to campaign when the code is invalid.
            String censusRedirect = getCensusRedirect(true)
            render view: '/campaignValidation/step0RegisterWithCensusCode_ERROR' , model:[redirectUrl:censusRedirect]
        }else{
            CampaignLightRSDTO campaign = censusLoginData.getCampaign();
            ContactRSDTO contact = censusLoginData.getContact();
            logoutIfContactDifferentAsLoggedUser(contact, censusLogin);
            log.info("[censusLogion: ${censusLogin}] : Receviced a valid censusLogin -> Contact: ${contact.email}")
            Evidences evidences = new HttpRequestRecoverEvidences(request);
            if (campaign.closed){
                // The closed view needs the OwnerTimeZone which is not mapped on camapaign ligh
                CampaignRSDTO campaignRSDTO = campaignService.find(campaign.getUser(), campaign.id, null)
                log.info("[censusLogion: ${censusLogin}] : Receviced a valid censusLogin -> Campaign is closed.");
                render view: '/campaignValidation/step0RegisterWithCensusCode_NOT_ACTIVE' , model:[redirectUrl:null, contact: contact, campaign:campaignRSDTO]
            } else if (springSecurityService.isLoggedIn()){
//                flash.message="You are already logged"
                log.info("[censusLogion: ${censusLogin}] : Receviced a valid censusLogin -> User is logged and is the same as the censusLogin.");
                KuorumUserRSDTO userFromContact = censusService.createUserByCensusCode(censusLogin, evidences); // THis method creates the user if not exists and then validates it.
                censusService.deleteCensusCode(censusLogin)
                redirect uri:calcNextStepMappingName(campaign)
            }else if (contact.getMongoId()){
                // If user already exists, instead of create he will be validated
                log.info("[censusLogion: ${censusLogin}] : User already exists. Recovering user and reauthenticate it ")
                KuorumUserRSDTO userFromContact = censusService.createUserByCensusCode(censusLogin, evidences);
                springSecurityService.reauthenticate userFromContact.getEmail()
                redirect uri:calcNextStepMappingName(campaign)
            }else{
                // DEFAULT
                log.info("[censusLogion: ${censusLogin}] : User doesn't exists. Showing contact data page ")
                render view: '/campaignValidation/step0RegisterWithCensusCode', model:[
                        contact: contact,
                        censusLogin:censusLogin,
                        campaign: campaign,
                        command:new KuorumRegisterCommand()]
            }
        }
    }

    private void logoutIfContactDifferentAsLoggedUser(ContactRSDTO contact, String censusLogin){
        if (springSecurityService.isLoggedIn()){
            KuorumUserSession userSession = springSecurityService.principal
            if (userSession.email != contact.email){
                log.info("[censusLogion: ${censusLogin}] : Logging out user (${userSession.email}) because it is using a censusLogin of different contact (${contact.email}")
                registerService.logout(request, response)
            }
        }
    }

    def step0RegisterWithCensusCodeSave(KuorumRegisterCommand command){

        String censusLogin = params['censusLogin'];
        CensusLoginRDTO censusLoginRDTO = censusService.getContactByCensusCode(censusLogin)

        if (!censusLoginRDTO){
            log.warn("[censusLogion: ${censusLogin}] : Invalid census login. Redirecting to home")
            flash.error="Your link is not valid. "
            redirect mapping:'home'
        }else{
            log.info("[censusLogion: ${censusLogin}] : Valid census login => Creating user")
            Evidences evidences = new HttpRequestRecoverEvidences(request);
            KuorumUserRSDTO userRSDTO = censusService.createUserByCensusCode(censusLogin,evidences);
            springSecurityService.reauthenticate userRSDTO.email
            redirect uri:calcNextStepMappingName(censusLoginRDTO.getCampaign())
        }
    }


    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationCensus(){
        log.info("Loading form of validation via CENSUS")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        [command: new DomainValidationCommand(), campaign: campaign]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationCensusSave(DomainValidationCommand command){
        log.info("Saving form of validation via CENSUS")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()){
            render view: "stepCampaignValidationCensus", model:[command:command,campaign: campaign]
            return
        }
        KuorumUserSession user =  springSecurityService.principal
        Evidences evidences = new HttpRequestRecoverEvidences(request);
        UserValidationRSDTO validationRSDTO = kuorumUserService.userDomainValidation(user, evidences, campaign.getId(), command.ndi, command.postalCode, command.birthDate)
        if (validationRSDTO.censusStatus.isGranted()){
            redirect uri:calcNextStepMappingName(campaign,validationRSDTO)
        }else{
            flash.error =g.message(code:'kuorum.web.commands.profile.DomainValidationCommand.validationError')
            render view: "stepCampaignValidationCensus", model:[command:command, campaign: campaign]
        }
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationCustomCode(){
        log.info("Loading form of validation via CODE")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        [command: new DomainUserCustomCodeValidationCommand(), campaign:campaign]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationCustomCodeSave(DomainUserCustomCodeValidationCommand command){
        log.info("Saving form of validation via CODE")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()){
            render view: "stepCampaignValidationCustomCode", model:[command:command,campaign:campaign]
            return
        }
        KuorumUserSession userSession =  springSecurityService.principal
        Evidences evidences = new HttpRequestRecoverEvidences(request);
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.userCodeDomainValidation(userSession, evidences, campaign.getId(), command.customCode)
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
        log.info("Loading form of validation via PHONE [input phone/show contact phone]")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        return modelInputPhoneValidationStep(null, campaign)
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def stepCampaignValidationPhoneCode(DomainUserPhoneValidationCommand command){
        log.info("Sending code for validation via PHONE ")
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
            render view: "stepCampaignValidationPhoneNumber", model:modelInputPhoneValidationStep(command, campaign)
            return
        }catch(Exception e){
            flash.message="Internal error. Try again or contact with info@kuorum.org"
            render view: "stepCampaignValidationPhoneNumber", model:modelInputPhoneValidationStep(command, campaign)
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
        log.info("Saving form of validation via PHONE [checking code]")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()){
            render view: "stepCampaignValidationPhoneCode", model:[command:command, campaign: campaign]
            return
        }
        KuorumUserSession userSession = springSecurityService.principal
        Evidences evidences = new HttpRequestRecoverEvidences(request);
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.userPhoneDomainValidation(userSession, evidences, campaign.getId(),command.validationPhoneNumberPrefix, command.validationPhoneNumber, command.phoneHash, command.phoneCode)
        if (userValidationRSDTO.phoneStatus.isGranted()){
            redirect uri:calcNextStepMappingName(campaign,userValidationRSDTO)
        }else{
            command.errors.rejectValue("phoneCode","kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.validationError");
            render view: "stepCampaignValidationPhoneCode", model:[command:command, campaign: campaign]
        }
    }


    private String calcNextStepMappingName(BasicCampaignInfoRSDTO campaignRSDTO, UserValidationRSDTO validationRSDTO=null) {
        if (springSecurityService.isLoggedIn()) {
            KuorumUserSession userSession = springSecurityService.principal
            if (!validationRSDTO) {
                validationRSDTO = kuorumUserService.getUserValidationStatus(userSession, campaignRSDTO.getId())
            }
            if (!validationRSDTO.censusStatus.granted) {
                return g.createLink(mapping: "campaignValidationCensus", params: campaignRSDTO.encodeAsLinkProperties());
            }else if (!validationRSDTO.codeStatus.granted){
                return g.createLink(mapping: "campaignValidationCode",params: campaignRSDTO.encodeAsLinkProperties());
            }else if (!validationRSDTO.phoneStatus.granted) {
                return g.createLink(mapping: "campaignValidationPhoneNumber", params: campaignRSDTO.encodeAsLinkProperties())
            }else if (campaignRSDTO){
                return g.createLink(mapping: "campaignShow", params: campaignRSDTO.encodeAsLinkProperties(), fragment: "survey-questions")
            }else {
                return getCensusRedirect(true);
            }
        } else {
            // NO LOGGED
            String censusRedirect = getCensusRedirect(true);
            log.info("Calculating redirect for an unlogged user: Campaign ${campaignRSDTO}")
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
