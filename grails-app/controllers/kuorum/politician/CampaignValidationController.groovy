package kuorum.politician

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.annotations.FunnelLoginSessionValid
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.domain.DomainService
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
import org.kuorum.rest.model.communication.BasicCampaignInfoRSDTO
import org.kuorum.rest.model.communication.CampaignLightRSDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.domain.DomainRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserExtraDataRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.UserPhoneValidationRDTO
import org.kuorum.rest.model.kuorumUser.validation.UserValidationRSDTO
import payment.campaign.CampaignService
import payment.contact.CensusService
import payment.contact.ContactService
import springSecurity.ExternIdJoinCommand
import springSecurity.KuorumRegisterCommand

class CampaignValidationController {
    KuorumUserService kuorumUserService
    CookieUUIDService cookieUUIDService
    CampaignService campaignService
    SpringSecurityService springSecurityService
    CensusService censusService
    RegisterService registerService
    ContactService contactService
    DomainService domainService

    private Boolean showLandingData = true;

    def initValidation() {
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        redirect uri: calcNextStepMappingName(campaign)
    }

    def step0RegisterWithCensusCode() {
        String censusLogin = params['censusLogin']
        CensusLoginRDTO censusLoginData = censusService.getContactByCensusCode(censusLogin)
        if (censusLoginData == null) {
            censusService.deleteCensusCode(censusLogin)
            log.info("[censusLogion: ${censusLogin}] : Receviced an invalid census login")
            // Sending to campaign when the code is invalid.
            String censusRedirect = getCensusRedirect(true)
            render view: '/campaignValidation/step0RegisterWithCensusCode_ERROR', model: [redirectUrl: censusRedirect]
        } else {
            CampaignLightRSDTO campaign = censusLoginData.getCampaign();
            ContactRSDTO contact = censusLoginData.getContact();
            logoutIfContactDifferentAsLoggedUser(contact, censusLogin);
            log.info("[censusLogion: ${censusLogin}] : Receviced a valid censusLogin -> Contact: ${contact.email}")
            Evidences evidences = new HttpRequestRecoverEvidences(request, cookieUUIDService.getBrowserId());
            if (campaign.closed) {
                // The closed view needs the OwnerTimeZone which is not mapped on camapaign ligh
                campaignClosedError(campaignService.find(campaign.getUser(), campaign.id, null), contact)
            } else if (springSecurityService.isLoggedIn()) {
//                flash.message="You are already logged"
                log.info("[censusLogion: ${censusLogin}] : Receviced a valid censusLogin -> User is logged and is the same as the censusLogin.");
                KuorumUserRSDTO userFromContact = censusService.createUserByCensusCode(censusLogin, evidences);
                // THis method creates the user if not exists and then validates it.
                censusService.deleteCensusCode(censusLogin)
                redirect uri: calcNextStepMappingNameWithUserRSDTO(campaign, userFromContact, ValidationStep.TOKEN)
            } else if (contact.getMongoId()) {
                if (showLandingData) {
                    log.info("[censusLogion: ${censusLogin}] : User exists, but we are going to show the contact data page ")
                    // Reathenticating to remove an external redirec
                    KuorumUserRSDTO userFromContact = kuorumUserService.findUserRSDTO(contact.getMongoId())

//                    springSecurityService.reauthenticate userFromContact.getEmailOrAlternative()
                    cookieUUIDService.buildAnonymousUser(userFromContact.getId().toString())
                    redirectToRegister0(contact, censusLogin, campaign, 'campaignValidationLinkCheck')

                } else {
                    log.info("[censusLogion: ${censusLogin}] : User already exists. Recovering user and reauthenticate it ")
                    KuorumUserRSDTO userFromContact = censusService.createUserByCensusCode(censusLogin, evidences);
//                    springSecurityService.reauthenticate userFromContact.getEmailOrAlternative()
                    cookieUUIDService.buildAnonymousUser(userFromContact.getId().toString())
                    redirect uri: calcNextStepMappingNameWithUserRSDTO(campaign, userFromContact, ValidationStep.TOKEN)
                }
            } else {
                // DEFAULT
                log.info("[censusLogion: ${censusLogin}] : User doesn't exists. Showing contact data page ")
                redirectToRegister0(contact, censusLogin, campaign, 'campaignValidationLinkCheck')
            }
        }
    }

    private redirectToRegister0(ContactRSDTO contact, String censusLogin, BasicCampaignInfoRSDTO campaign, String formMapping) {
        render view: '/campaignValidation/step0RegisterWithCensusCode', model: [
                contact    : contact,
                censusLogin: censusLogin,
                campaign   : campaign,
                formMapping: formMapping,
                domain     : CustomDomainResolver.getDomainRSDTO(),
                command    : new KuorumRegisterCommand()]
    }

    def step0RegisterWithExternalId() {
        CampaignRSDTO campaign = campaignService.find(params.get("ownerId"), Long.valueOf(params.get("campaignId")), null)
        if(!campaign.isQrEnabled()){
            qrDisabledRedirect()
            return
        }
        if (campaign) {
            ContactRSDTO contact = contactService.getContactByExternalId(campaign.getUser().getId(), params.get("externalId"))
            if (contact) {

                logoutIfContactDifferentAsLoggedUser(contact, null);
                log.info("[externalIdJoin: ${params.get("externalId")}] : Receviced a valid Contact: ${contact.name},${contact.email}")
                Evidences evidences = new HttpRequestRecoverEvidences(request, cookieUUIDService.getBrowserId());

                if (campaign.closed) {
                    campaignClosedError(campaign, contact)

                } else if (springSecurityService.isLoggedIn()) {
                    log.info("[externalIdJoin: ${contact}] : Receviced a valid contact -> User is logged and is the same as the censusLogin.");
                    KuorumUserRSDTO userFromContact = censusService.createUserByExternalId(contact, evidences, campaign.user.id, campaign.id);
                    // THis method creates the user if not exists and then validates it.
                    redirect uri: calcNextStepMappingNameWithUserRSDTO(campaign, userFromContact, ValidationStep.EXTERNAL_ID)

                } else if (contact.getMongoId()) {
                    if (showLandingData) {
                        log.info("[externalIdJoin: ${contact}] : User exists, but we are going to show the contact data page ")
                        // Reathenticating to remove an external redirec
                        KuorumUserRSDTO userFromContact = kuorumUserService.findUserRSDTO(contact.getMongoId())
//                        springSecurityService.reauthenticate userFromContact.getEmailOrAlternative()
                        cookieUUIDService.buildAnonymousUser(userFromContact.getId().toString())
                        redirectToRegister0(contact, null, campaign, 'campaignValidationLinkCheckExternal')

                    } else {
                        log.info("[externalIdJoin: ${contact}] : User already exists. Recovering user and reauthenticate it ")
                        KuorumUserRSDTO userFromContact = censusService.createUserByExternalId(contact, evidences, campaign.user.id, campaign.id);
//                        springSecurityService.reauthenticate userFromContact.getEmailOrAlternative()
                        cookieUUIDService.buildAnonymousUser(userFromContact.getId())
                        redirect uri: calcNextStepMappingNameWithUserRSDTO(campaign, userFromContact, ValidationStep.EXTERNAL_ID)
                    }
                } else {
                    // DEFAULT
                    log.info("[externalIdJoin: ${contact}] : User doesn't exists. Showing contact data page ")
                    redirectToRegister0(contact, null, campaign, 'campaignValidationLinkCheckExternal')
                }
            } else {
                ExternIdJoinCommand command = new ExternIdJoinCommand(campaignId: campaign.id, ownerId: campaign.user.id)
                command.errors.rejectValue("externalId", "kuorum.langings.join.external.error")
                render view: '/register/joinCheck', model: [command: command, labelExternalId: CustomDomainResolver.domainRSDTO.externalIdName, campaign: campaign]
            }
        } else {
            flash.error = message(code: "kuorum.langings.join.external.error")
            redirect uri: g.createLink(mapping: "joinDomain")
        }
    }

    private void campaignClosedError(CampaignRSDTO campaign, ContactRSDTO contact) {
        if (campaign.getStartDate() != null && campaign.getStartDate().after(new Date())) {
            // CAMPAIGN NOT OPEN
            render view: '/campaignValidation/step0RegisterWithCensusCode_NOT_ACTIVE', model: [redirectUrl: null, contact: contact, campaign: campaign]
            log.info("[contactLogion: ${contact.email}] : Receviced a valid censusLogin -> Campaign is not open.");
        } else {
            // CAMPAIGN FINISHED
            log.info("[contactLogion: ${contact.email}] : Receviced a valid censusLogin -> Campaign is finished.");
            render view: '/campaignValidation/campaignClosed', model: [redirectUrl: null, contact: contact, campaign: campaign]
        }
    }

    private void logoutIfContactDifferentAsLoggedUser(ContactRSDTO contact, String censusLogin) {
        if (springSecurityService.isLoggedIn()) {
            KuorumUserSession userSession = springSecurityService.principal
            if (userSession.email != contact.email) {
                log.info("[censusOrExternalIdLogion: ${censusLogin ?: userSession.name}] : Logging out user (${userSession.email}) because it is using a censusLogin of different contact (${contact.email}")
                registerService.logout(request, response)
            }
        }
    }

    def step0RegisterWithCensusCodeSave(KuorumRegisterCommand command) {
        String censusLogin = params['censusLogin'];
        CensusLoginRDTO censusLoginRDTO = censusService.getContactByCensusCode(censusLogin)

        if (!censusLoginRDTO) {
            log.warn("[censusLogion: ${censusLogin}] : Invalid census login. Redirecting to home")
            flash.error = "Your link is not valid. "
            redirect mapping: 'home'
        } else {
            log.info("[censusLogion: ${censusLogin}] : Valid census login => Creating user")
            Evidences evidences = new HttpRequestRecoverEvidences(request, cookieUUIDService.getBrowserId());
            KuorumUserRSDTO userRSDTO = censusService.createUserByCensusCode(censusLogin, evidences);
//            springSecurityService.reauthenticate userRSDTO.getEmailOrAlternative()
            cookieUUIDService.buildAnonymousUser(userRSDTO.getId())
            censusService.deleteCensusCode(censusLogin)
            redirect uri: calcNextStepMappingNameWithUserRSDTO(censusLoginRDTO.getCampaign(), userRSDTO, ValidationStep.CENSUS)
        }
    }

    def step0RegisterWithExternalIdSave(KuorumRegisterCommand command) {
        CampaignRSDTO campaign = campaignService.find(command.ownerId, command.campaignId.toLong())
        if (!campaign.isQrEnabled()) {
            qrDisabledRedirect()
            return
        }
        log.info("[externalIdLogion: ${command.externalId}] : Valid contact login => Creating user")
        Evidences evidences = new HttpRequestRecoverEvidences(request, cookieUUIDService.getBrowserId());
        try {
            KuorumUserRSDTO userRSDTO = censusService.createUserByExternalId(contactService.getContactByExternalId(command.ownerId, command.externalId), evidences, command.ownerId, command.campaignId.toLong());
//            springSecurityService.reauthenticate userRSDTO.getEmailOrAlternative()
            cookieUUIDService.buildAnonymousUser(userRSDTO.getId())
            ValidationStep nextStep = ValidationStep.EXTERNAL_ID
            redirect uri: calcNextStepMappingNameWithUserRSDTO(campaign, userRSDTO, nextStep, null)
        } catch (Exception e) {
            log.error("Exception Error creating user using External Id", e)
            flash.error = "Your data is not valid. "
            redirect mapping: 'home'
            return;
        }
    }


    @FunnelLoginSessionValid
    def stepCampaignValidationCensus() {
        log.info("Loading form of validation via CENSUS")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        [command: new DomainValidationCommand(), campaign: campaign]
    }

    @FunnelLoginSessionValid
    def stepCampaignValidationCensusSave(DomainValidationCommand command) {
        log.info("Saving form of validation via CENSUS")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()) {
            render view: "stepCampaignValidationCensus", model: [command: command, campaign: campaign]
            return
        }
//        KuorumUserSession user = springSecurityService.principal
        KuorumUserSession user = recoverUserSessionDependingOnCookieOrSession();
        Evidences evidences = new HttpRequestRecoverEvidences(request, cookieUUIDService.getBrowserId());
        UserValidationRSDTO validationRSDTO = kuorumUserService.userDomainValidation(user, evidences, campaign.getId(), command.ndi, command.postalCode, command.birthDate)
        if (validationRSDTO.censusStatus.isGranted()) {
            redirect uri: calcNextStepMappingNameWithUsersSession(campaign, user, ValidationStep.CENSUS, validationRSDTO)
        } else {
            flash.error = g.message(code: 'kuorum.web.commands.profile.DomainValidationCommand.validationError')
            render view: "stepCampaignValidationCensus", model: [command: command, campaign: campaign]
        }
    }

    @FunnelLoginSessionValid
    def stepCampaignValidationCustomCode() {
        log.info("Loading form of validation via CODE")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        [command: new DomainUserCustomCodeValidationCommand(), campaign: campaign]
    }


    @FunnelLoginSessionValid
    def stepCampaignValidationCustomCodeSave(DomainUserCustomCodeValidationCommand command) {
        log.info("Saving form of validation via CODE")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()) {
            render view: "stepCampaignValidationCustomCode", model: [command: command, campaign: campaign]
            return
        }
//        KuorumUserSession userSession = springSecurityService.principal
        KuorumUserSession userSession = recoverUserSessionDependingOnCookieOrSession();
        Evidences evidences = new HttpRequestRecoverEvidences(request, cookieUUIDService.getBrowserId());
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.userCodeDomainValidation(userSession, evidences, campaign.getId(), command.customCode)
        String msg;
        if (userValidationRSDTO.codeStatus.isGranted()) {
            msg = "Success validation"
        } else {
            msg = g.message(code: "kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.customCode.validationError", args: [userSession.email.replaceFirst(/([^@]{3}).*@(..).*/, "\$1***@\$2***")])
            log.info("Error validating user: ${msg}")
        }

        if (userValidationRSDTO.codeStatus.isGranted()) {
            redirect uri: calcNextStepMappingNameWithUsersSession(campaign, userSession, ValidationStep.CODE, userValidationRSDTO)
        } else {
            command.errors.rejectValue('customCode', "validationError", msg)
            render view: "stepCampaignValidationCustomCode", model: [command: command, campaign: campaign]
        }
    }

    @FunnelLoginSessionValid
    def stepCampaignValidationPhoneNumber() {
        log.info("Loading form of validation via PHONE [input phone/show contact phone]")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        return modelInputPhoneValidationStep(null, campaign)
    }

    @FunnelLoginSessionValid
    def stepCampaignValidationPhoneCode(DomainUserPhoneValidationCommand command) {
        log.info("Sending code for validation via PHONE ")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()) {
            render view: "stepCampaignValidationPhoneNumber", model: modelInputPhoneValidationStep(command, campaign)
            return
        }
//        KuorumUserSession userSession = springSecurityService.principal
        KuorumUserSession userSession = recoverUserSessionDependingOnCookieOrSession();
        try {
            UserPhoneValidationRDTO userPhoneValidationRDTO = kuorumUserService.sendSMSWithValidationCode(
                    userSession,
                    campaign.getId(),
                    command.phoneNumber.toString(),
                    command.phoneNumberPrefix,
                    cookieUUIDService.getBrowserId())
            [command : new DomainUserPhoneCodeValidationCommand(
                    phoneHash: userPhoneValidationRDTO.getHash(),
                    validationPhoneNumberPrefix: userPhoneValidationRDTO.getPhoneNumberPrefix(),
                    validationPhoneNumber: userPhoneValidationRDTO.getPhoneNumber()),
             campaign: campaign]
        } catch (KuorumException e) {
            command.errors.rejectValue("phoneNumber", 'kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumber.repeatedNumber')
            render view: "stepCampaignValidationPhoneNumber", model: modelInputPhoneValidationStep(command, campaign)
            return
        } catch (Exception e) {
            flash.message = "Internal error. Try again or contact with info@kuorum.org"
            render view: "stepCampaignValidationPhoneNumber", model: modelInputPhoneValidationStep(command, campaign)
            return
        }
    }

    private def modelInputPhoneValidationStep(DomainUserPhoneValidationCommand command, CampaignRSDTO campaign) {
        if (command == null) {
            command = new DomainUserPhoneValidationCommand()
        }
//        KuorumUserSession userSession = springSecurityService.principal
        KuorumUserSession userSession = recoverUserSessionDependingOnCookieOrSession();
        KuorumUserExtraDataRSDTO extraDataRSDTO = kuorumUserService.findUserExtendedDataRSDTO(userSession)
        String phone = extraDataRSDTO.phoneNumber?.encodeAsHiddenPhone()
        Boolean predefinedPhone = extraDataRSDTO.phoneNumber ? true : false;
        return [predefinedPhone: predefinedPhone, phone: phone, command: command, campaign: campaign]
    }

    @FunnelLoginSessionValid
    def stepCampaignValidationPhoneCodeSave(DomainUserPhoneCodeValidationCommand command) {
        log.info("Saving form of validation via PHONE [checking code]")
        CampaignRSDTO campaign = getCampaignRSDTO(params)
        if (command.hasErrors()) {
            render view: "stepCampaignValidationPhoneCode", model: [command: command, campaign: campaign]
            return
        }
//        KuorumUserSession userSession = springSecurityService.principal
        KuorumUserSession userSession = recoverUserSessionDependingOnCookieOrSession();
        Evidences evidences = new HttpRequestRecoverEvidences(request, cookieUUIDService.getBrowserId());
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.userPhoneDomainValidation(userSession, evidences, campaign.getId(), command.validationPhoneNumberPrefix, command.validationPhoneNumber, command.phoneHash, command.phoneCode)
        if (userValidationRSDTO.phoneStatus.isGranted()) {
            redirect uri: calcNextStepMappingNameWithUsersSession(campaign, userSession, ValidationStep.PHONE_SAVE, userValidationRSDTO)
        } else {
            command.errors.rejectValue("phoneCode", "kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.validationError");
            render view: "stepCampaignValidationPhoneCode", model: [command: command, campaign: campaign]
        }
    }


    private String calcNextStepMappingNameWithUsersSession(BasicCampaignInfoRSDTO campaignRSDTO, KuorumUserSession user, ValidationStep currentStep = null, UserValidationRSDTO validationRSDTO = null) {
        return calcNextStepMappingName(campaignRSDTO, user.getId(), currentStep, validationRSDTO)
    }

    private String calcNextStepMappingNameWithUserRSDTO(BasicCampaignInfoRSDTO campaignRSDTO, KuorumUserRSDTO user = null, ValidationStep currentStep = null, UserValidationRSDTO validationRSDTO = null) {
        return calcNextStepMappingName(campaignRSDTO, user.getId(), currentStep, validationRSDTO)
    }

    private String calcNextStepMappingName(BasicCampaignInfoRSDTO campaignRSDTO, String userId = null, ValidationStep currentStep = null, UserValidationRSDTO validationRSDTO = null) {
        KuorumUserSession fakeUserSession = recoverUserSessionDependingOnCookieOrSession(userId)
        DomainRSDTO domainRSDTO = CustomDomainResolver.getDomainRSDTO()
        if (fakeUserSession) {
            ValidationStep nextValidationStep = nextStep(campaignRSDTO, domainRSDTO, currentStep, validationRSDTO)
            if (nextValidationStep) {
                // USER IS NOT STILL VALIDATED
                return g.createLink(mapping: nextValidationStep.getUrlMappingNameNextStep(), params: campaignRSDTO.encodeAsLinkProperties());
            } else {
                // USER VALIDATED
                KuorumUserRSDTO userRSDTO = kuorumUserService.findUserRSDTO(fakeUserSession)
                springSecurityService.reauthenticate userRSDTO.getEmailOrAlternative()
                if (campaignRSDTO) {
                    return g.createLink(mapping: "campaignShow", params: campaignRSDTO.encodeAsLinkProperties(), fragment: "survey-questions")
                } else {
                    return getCensusRedirect(true);
                }
            }
        } else {
            // NO LOGGED OR NOT INITIALIZED THE LOGIN PROCESS
            String censusRedirect = getCensusRedirect(true);
            log.info("Calculating redirect for an unlogged user: Campaign ${campaignRSDTO}")
            if (campaignRSDTO) {
                return g.createLink(mapping: "campaignShow", params: campaignRSDTO.encodeAsLinkProperties())
            } else {
                return censusRedirect;
            }
        }
    }

    private ValidationStep nextStep(BasicCampaignInfoRSDTO campaignRSDTO, DomainRSDTO domainRSDTO, ValidationStep currentStep, UserValidationRSDTO validationRSDTO = null) {
        KuorumUserSession userSession = recoverUserSessionDependingOnCookieOrSession()
        if (!validationRSDTO) {
            validationRSDTO = kuorumUserService.getUserValidationStatus(userSession, campaignRSDTO.getId())
        }
        if (!springSecurityService.isLoggedIn() && validationRSDTO.isGranted()) {
            KuorumUserRSDTO userRSDTO = kuorumUserService.findUserRSDTO(userSession)
            springSecurityService.reauthenticate userRSDTO.getEmailOrAlternative()
            return null;
        } else if (springSecurityService.isLoggedIn()) {
            return null;
        }
        return currentStep.getNextStep(validationRSDTO)
    }

    public enum ValidationStep {
        TOKEN("validation", "", ""),
        EXTERNAL_ID("validation", "", ""),
        CENSUS("validationCensus", "censusStatus", "campaignValidationCensus"),
        CODE("validationCode", "codeStatus", "campaignValidationCode"),
        PHONE_SEND("validationPhone", "phoneStatus", "campaignValidationPhoneNumber"),
        PHONE_SAVE("validationPhone", "phoneStatus", "campaignValidationPhoneNumber");

        // Done in static because it is not possible to autoreference itself in the constructor.
        static {
            TOKEN.nextStep = CENSUS;
            EXTERNAL_ID.nextStep = CENSUS;
            CENSUS.nextStep = CODE;
            CODE.nextStep = PHONE_SEND;
            PHONE_SEND.nextStep = PHONE_SAVE;
            PHONE_SAVE.nextStep = null;
        }

        ValidationStep nextStep;
        String domainRsdtoConfigField
        String userValidationField
        String urlMappingNameNextStep

        ValidationStep(String domainRsdtoConfigField, String userValidationField, String urlMappingNameNextStep) {
            this.domainRsdtoConfigField = domainRsdtoConfigField
            this.userValidationField = userValidationField
            this.urlMappingNameNextStep = urlMappingNameNextStep
        }

        ValidationStep getNextStep(DomainRSDTO domainRSDTO) {
            ValidationStep possibleNextStep = this.nextStep;
            // GET NEXT STEP CHECKING DOMAIN CONFIG. If domain config says that it is not active returns the next one or null if there aren't any
            return possibleNextStep && !domainRSDTO."${possibleNextStep.domainRsdtoConfigField}" ? possibleNextStep.getNextStep(domainRSDTO) : possibleNextStep
        }

        ValidationStep getNextStep(UserValidationRSDTO validationRSDTO) {
            ValidationStep possibleNextStep = this.nextStep;
            // GET NEXT STEP CHECKING DOMAIN CONFIG. If it is validated, returns the next step
            return possibleNextStep && validationRSDTO."${possibleNextStep.userValidationField}".granted ? possibleNextStep.getNextStep(validationRSDTO) : possibleNextStep
        }
    }

    private KuorumUserSession recoverUserSessionDependingOnCookieOrSession(String userId = null) {
        if (springSecurityService.isLoggedIn()) {
            return springSecurityService.principal
        } else if (userId) {
            return cookieUUIDService.buildAnonymousUser(userId);
        } else if (cookieUUIDService.isUserUUIDSet()) {
            return cookieUUIDService.buildAnonymousUser(cookieUUIDService.getUserUUID());
        } else
            return null
    }

    private CampaignRSDTO getCampaignRSDTO(def params) {
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        CampaignRSDTO campaignRSDTO = campaignService.find(user, Long.parseLong(params.campaignId))
        if (!campaignRSDTO) {
            throw new KuorumException(message(code: "post.notFound") as String)
        }
        return campaignRSDTO;
    }

    private String getCensusRedirect(Boolean deleteCookie) {
        String censusRedirect = cookieUUIDService.getDomainCookie(WebConstants.COOKIE_URL_CALLBACK_CENSUS_LOGIN)
        if (deleteCookie) {
            cookieUUIDService.deleteDomainCookie(WebConstants.COOKIE_URL_CALLBACK_CENSUS_LOGIN)
        }
        if (!censusRedirect) {
            censusRedirect = g.createLink(mapping: "home");
        }
        return censusRedirect
    }

    private void qrDisabledRedirect() {
        log.warn("Try to use qr validation on a don't enable domain");
        flash.error = message(code: "landingPage.join.enabled.error")
        redirect uri: g.createLink(mapping: "home")
    }


    private class UserIdFunnelLoginStorage {
        private static final String COOKIE_USER_ID_SAVED_NAME = "login_funnel_id";

        public void save(String userId) {
            cookieUUIDService
        }

        public String recover() {

        }
    }


    def campaignUserValidChecker() {
        Long campaignId = Long.parseLong(params.campaignId)
        def result = userValidationChecker(campaignId)
        render(result as JSON)
    }

    // USER VALIDATION
    def domainUserValidChecker() {
        Long campaignId = Long.parseLong(params.campaignId)
        def result = userValidationChecker(campaignId)
        render(result as JSON)
    }

    private def userValidationChecker(Long campaignId) {
        KuorumUserSession userSession = recoverUserSessionDependingOnCookieOrSession();
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.getUserValidationStatus(userSession, campaignId)
        if (userValidationRSDTO.isGranted()) {
            KuorumUserRSDTO userRSDTO = kuorumUserService.findUserRSDTO(userSession)
            springSecurityService.reauthenticate userRSDTO.getEmailOrAlternative()
        }
        return [validated: userValidationRSDTO.isGranted(), success: true, pendingValidations: kuorumUserService.getPendingValidations(userValidationRSDTO)]
    }
}
