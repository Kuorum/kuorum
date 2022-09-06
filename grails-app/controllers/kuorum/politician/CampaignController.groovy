package kuorum.politician

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import groovy.time.TimeCategory
import kuorum.KuorumFile
import kuorum.core.FileType
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.files.FileService
import kuorum.register.KuorumUserSession
import kuorum.security.evidences.Evidences
import kuorum.security.evidences.HttpRequestRecoverEvidences
import kuorum.solr.SearchSolrService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.util.TimeZoneUtil
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand
import kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand
import kuorum.web.commands.profile.DomainUserPhoneValidationCommand
import kuorum.web.commands.profile.DomainValidationCommand
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.*
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.event.EventRDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRDTO
import org.kuorum.rest.model.communication.search.SearchCampaignRDTO
import org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO
import org.kuorum.rest.model.communication.survey.SurveyRDTO
import org.kuorum.rest.model.communication.survey.SurveyVoteTypeDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.filter.FilterRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserExtraDataRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.UserPhoneValidationRDTO
import org.kuorum.rest.model.kuorumUser.validation.UserValidationRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.search.SearchKuorumElementRSDTO
import org.kuorum.rest.model.search.SearchParamsRDTO
import org.kuorum.rest.model.search.SearchResultsRSDTO
import org.kuorum.rest.model.search.SearchTypeRSDTO
import payment.campaign.*
import payment.contact.ContactService

import javax.servlet.http.HttpServletResponse

class CampaignController {

    PostService postService
    DebateService debateService
    SurveyService surveyService
    ContactService contactService
    ParticipatoryBudgetService participatoryBudgetService
    ContestService contestService
    ContestApplicationService contestApplicationService
    DistrictProposalService districtProposalService
    PetitionService petitionService
    SpringSecurityService springSecurityService
    FileService fileService

    KuorumUserService kuorumUserService
    CookieUUIDService cookieUUIDService
    CampaignService campaignService
    SearchSolrService searchSolrService

    def show() {
        String viewerUid = cookieUUIDService.buildUserUUID()
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        try {
            CampaignRSDTO campaignRSDTO = campaignService.find(user, Long.parseLong(params.campaignId), viewerUid)
            if (!campaignRSDTO) {
                throw new KuorumException(message(code: "post.notFound") as String)
            }
            def dataView = [view: null, model: null]
            switch (campaignRSDTO.campaignType) {
                case CampaignTypeRSDTO.DEBATE:
                    dataView = debateService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                case CampaignTypeRSDTO.POST:
                    dataView = postService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                case CampaignTypeRSDTO.SURVEY:
                    dataView = surveyService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                case CampaignTypeRSDTO.PARTICIPATORY_BUDGET:
                    dataView = participatoryBudgetService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                case CampaignTypeRSDTO.DISTRICT_PROPOSAL:
                    dataView = districtProposalService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                case CampaignTypeRSDTO.PETITION:
                    dataView = petitionService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                case CampaignTypeRSDTO.CONTEST:
                    dataView = contestService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                case CampaignTypeRSDTO.CONTEST_APPLICATION:
                    dataView = contestApplicationService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                default:
                    log.error("Campaign type not recognized: ${campaignRSDTO.campaignType}")
                    throw new Exception("Campaign type not recognized: ${campaignRSDTO.campaignType}")
            }
            def model = dataView.model
            List<String> linkFiles = campaignService.getFiles(campaignRSDTO);
            model.campaignFiles = linkFiles.collect { it -> it.encodeAsS3File() }
            if (SpringSecurityUtils.ifAllGranted("ROLE_ADMIN")) {
                // Only admins recovers the contact info. This reduce the loading time requesting stupid info
                ContactRSDTO contactRSDTO = campaignService.getContactData(campaignRSDTO, viewerUid);
                model.contact = contactRSDTO
                List<String> linkContactFiles = campaignService.getContactFiles(campaignRSDTO, viewerUid);
                model.contactFiles = linkContactFiles.collect { it -> it.encodeAsS3File() }
            }
            if (springSecurityService.isLoggedIn()) {
                KuorumUserSession userLogged = springSecurityService.principal
                model.displayTimeZone = userLogged.timeZone ?: campaignRSDTO.user.timeZone
            } else {
                model.displayTimeZone = campaignRSDTO.user.timeZone
            }
            render view: dataView.view, model: dataView.model
        } catch (Exception ignored) {
            flash.error = message(code: "post.notFound")
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
    }

    def findLiUserCampaigns(String userId) {
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(userId)

        List<SearchTypeRSDTO> campaignTypes = new ArrayList<SearchTypeRSDTO>(Arrays.asList(SearchTypeRSDTO.values()));
        campaignTypes.remove(SearchTypeRSDTO.KUORUM_USER);

        SearchParamsRDTO searchParamsRDTO = new SearchParamsRDTO(
                page: 0,
                size: 10,
                ownerAlias: [user.alias],
                types: campaignTypes);
        SearchResultsRSDTO campaigns = searchSolrService.searchAPI(searchParamsRDTO)
        List<SearchKuorumElementRSDTO> userCampaigns = campaigns.data
        render template: '/campaigns/cards/campaignsList', model: [campaigns: userCampaigns, showAuthor: true]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def findMyActiveCampaigns() {
        KuorumUserSession user = springSecurityService.principal
        // TODO: BAD TRICK -> Recover all campaigns without pagination
        SearchCampaignRDTO searchCampaignRDTO = new SearchCampaignRDTO(
                page: 0,
                size: 1000,
                attachNotPublished: false,
                onlyPublications: true
        )
        CampaignLightPageRSDTO adminCampaigns = campaignService.findAllCampaigns(user, searchCampaignRDTO)
        render adminCampaigns.data as JSON
    }

    protected def modelSettings(CampaignSettingsCommand command, CampaignRSDTO campaignRSDTO = null) {
        KuorumUserSession user = springSecurityService.principal
        List<FilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        if (campaignRSDTO) {
            command.campaignName = campaignRSDTO.name
            command.tags = campaignRSDTO.triggeredTags
            command.filterId = campaignRSDTO.newsletter?.filter?.id
            command.validationType = campaignRSDTO.validationType
            command.groupValidation = campaignRSDTO.groupValidation
            command.campaignVisibility = campaignRSDTO.campaignVisibility
            command.newsletterCommunication = campaignRSDTO.newsletterCommunication
            command.profileComplete = campaignRSDTO.profileComplete
            command.endDate = campaignRSDTO.endDate
            command.startDate = campaignRSDTO.startDate
            if (campaignRSDTO.hasProperty('causes')) {
                command.causes = campaignRSDTO.causes
            }
            FilterRSDTO currentFilter = campaignRSDTO.newsletter?.filter;
            if (currentFilter && !filters.find { it.id == currentFilter.id }) {
//              If current filter is not in the user'f filters, then it is a anonymous filter. Adding it to the list of filter to be displayed
                filters.add(currentFilter)
            }
        } else {
            command.validationType = CampaignValidationTypeRDTO.NONE
        }
        [
                filters         : filters,
                command         : command,
                totalContacts   : contactPageRSDTO.total,
                campaign        : campaignRSDTO,
                status          : campaignRSDTO?.newsletter?.status ?: null,
                domainValidation: CustomDomainResolver.domainRSDTO?.validation
        ]
    }

    protected FilterRDTO recoverAnonymousFilterSettings(params, CampaignSettingsCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        contactService.transformCommand(filterCommand, "Custom filter for ${command.campaignName}")
    }

    protected mapCommandSettingsToRDTO(KuorumUserSession user, CampaignRDTO rdto, CampaignSettingsCommand command, FilterRDTO anonymousFilter) {
        rdto.name = command.campaignName
        if (!rdto.title) {
            rdto.title = command.campaignName;
        }
        rdto.setTriggeredTags(command.tags)
        rdto.causes = command.causes
        if (CustomDomainResolver.domainRSDTO?.validation) {
            // Only if domain validation is active, then the validationType of the campaign is editable
            rdto.validationType = command.validationType
        } else {
            rdto.validationType = CampaignValidationTypeRDTO.NONE
        }
        rdto.campaignVisibility = command.campaignVisibility == null ? CampaignVisibilityRSDTO.NON_VISIBLE : command.campaignVisibility
        rdto.groupValidation = command.groupValidation == null ? false : command.groupValidation
        rdto.newsletterCommunication = command.newsletterCommunication == null ? false : command.newsletterCommunication
        rdto.profileComplete = command.profileComplete == null ? false : command.profileComplete
        rdto.startDate = TimeZoneUtil.convertToUserTimeZone(command.startDate, user.timeZone)
        rdto.endDate = TimeZoneUtil.convertToUserTimeZone(command.endDate, user.timeZone)
        if (command.filterEdited) {
            //anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            rdto.setAnonymousFilter(anonymousFilter)
            rdto.setFilterId(null)
        } else {
            rdto.setFilterId(command.filterId)
        }
        if (command.eventAttached && !rdto.event) {
            rdto.event = new EventRDTO()
        }
        if (rdto instanceof SurveyRDTO && (grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN") || grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_ADMIN"))) {
            //Custom logic of survey. May be this logic shouldn't be here
            rdto.voteType = command.voteType == null ? SurveyVoteTypeDTO.MANIFEST : command.voteType;
            rdto.signVotes = command.signVotes == null ? false : command.signVotes;
        }
        rdto
    }

    protected CampaignRDTO createRDTO(KuorumUserSession user, Long campaignId, CampaignCreatorService campaignService) {
        CampaignRSDTO campaignRSDTO = campaignService.find(user, campaignId)
        return campaignService.map(campaignRSDTO)
    }

    protected CampaignRDTO convertCommandSettingsToRDTO(
            CampaignSettingsCommand command,
            KuorumUserSession user,
            FilterRDTO anonymousFilter,
            Long campaignId,
            CampaignCreatorService campaignService) {
        CampaignRDTO campaignRDTO = createRDTO(user, campaignId, campaignService)
        mapCommandSettingsToRDTO(user, campaignRDTO, command, anonymousFilter)
    }

    protected Map<String, Object> saveCampaignSettings(
            CampaignSettingsCommand command,
            def params,
            CampaignCreatorService campaignService) {
        KuorumUserSession user = springSecurityService.principal
        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        FilterRDTO anonymousFilter = recoverAnonymousFilterSettings(params, command)

        CampaignRDTO campaignRDTO = convertCommandSettingsToRDTO(command, user, anonymousFilter, campaignId, campaignService)
        CampaignRSDTO campaignSaved = campaignService.save(user, campaignRDTO, campaignId)
        String msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [campaignSaved.title])

        def nextStep = [
                mapping: params.redirectLink,
                params : campaignSaved.encodeAsLinkProperties()
        ]

        [msg: msg, campaign: campaignSaved, nextStep: nextStep]
    }

    protected CampaignRSDTO setCampaignAsDraft(Long campaignId, CampaignCreatorService campaignService) {
        KuorumUserSession user = springSecurityService.principal
        CampaignRSDTO campaignRSDTO = campaignService.find(user, campaignId)
        if (campaignRSDTO && campaignRSDTO.newsletter.status == CampaignStatusRSDTO.SCHEDULED) {
            CampaignRDTO campaignRDTO = campaignService.map(campaignRSDTO)
            campaignRDTO.setPublishOn(null)
            campaignService.save(user, campaignRDTO, campaignId)
        }
        return campaignRSDTO

    }

    protected def campaignModelContent(Long campaignId, CampaignRSDTO campaignRSDTO = null, CampaignContentCommand command = null, CampaignCreatorService campaignService) {

        KuorumUserSession user = springSecurityService.principal
        if (!campaignRSDTO && campaignId) {
            campaignRSDTO = campaignService.find(user, campaignId)
        }

        if (campaignRSDTO?.event && !campaignRSDTO.event.latitude) {
            // Debate has an event attached but is not defined the place.
            // Redirects to edit event
            flash.message = g.message(code: 'tools.massMailing.event.advise.empty')
            String mapping = campaignRSDTO instanceof DebateRSDTO ? 'debateEditEvent' : 'postEditEvent'
            redirect mapping: mapping, params: campaignRSDTO.encodeAsLinkProperties()
            return
        }

        if (!command) {
            command = new CampaignContentCommand()
            if (campaignRSDTO) {
                command.title = campaignRSDTO.title
                command.body = campaignRSDTO.body
                //Links are encoded Hopefully, user not use URL encoding in his texts
                if (campaignRSDTO.videoUrl) {
                    command.videoPost = campaignRSDTO.videoUrl
                    command.fileType = FileType.YOUTUBE.toString()
                }

                if (campaignRSDTO.datePublished) {
                    command.publishOn = campaignRSDTO.datePublished
                }

                if (campaignRSDTO.photoUrl) {
                    KuorumFile kuorumFile = KuorumFile.findByUrl(campaignRSDTO.photoUrl)
                    command.headerPictureId = kuorumFile?.id
                    command.fileType = FileType.IMAGE.toString()
                }
            }
        }
        Long numberRecipients = getCampaignNumberRecipients(user, campaignRSDTO)
        [
                command         : command,
                numberRecipients: numberRecipients,
                campaign        : campaignRSDTO,
                status          : campaignRSDTO?.campaignStatusRSDTO ?: CampaignStatusRSDTO.DRAFT
        ]
    }

    protected Long getCampaignNumberRecipients(KuorumUserSession user, CampaignRSDTO campaignRSDTO) {
        if (campaignRSDTO.newsletterCommunication) {
            Long numberRecipients = campaignRSDTO?.newsletter?.filter?.amountOfContacts != null ?
                    campaignRSDTO.newsletter?.filter?.amountOfContacts :
                    contactService.getUsers(user, null).total
            return numberRecipients
        } else {
            return 0L;
        }
    }

    protected CampaignRDTO convertCommandContentToRDTO(CampaignContentCommand command, KuorumUserSession user, Long campaignId, CampaignCreatorService campaignService) {
        CampaignRDTO campaignRDTO = createRDTO(user, campaignId, campaignService)
        campaignRDTO.title = command.title
        campaignRDTO.body = command.body

        // Multimedia URL
        if (command.fileType == FileType.IMAGE.toString() && command.headerPictureId) {
            // Save image
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture.setCampaignId(campaignId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            campaignRDTO.setPhotoUrl(picture.getUrl())

            // Remove video
            campaignRDTO.setVideoUrl(null)
        } else if (command.fileType == FileType.YOUTUBE.toString() && command.videoPost) {
            // Save video
            String youtubeUrl = command.videoPost.encodeAsYoutubeName();
            campaignRDTO.setVideoUrl(youtubeUrl)

            // Remove image
            if (command.headerPictureId) {
                KuorumFile picture = KuorumFile.get(command.headerPictureId)
                fileService.deleteKuorumFile(picture)
                command.setHeaderPictureId(null)
                campaignRDTO.setPhotoUrl(null)
            }
        }
        campaignRDTO
    }

    protected Map<String, Object> saveAndSendCampaignContent(CampaignContentCommand command, Long campaignId, CampaignCreatorService campaignService) {
        KuorumUserSession user = springSecurityService.principal
        CampaignRDTO campaignRDTO = convertCommandContentToRDTO(command, user, campaignId, campaignService)
        saveAndSendCampaign(user, campaignRDTO, campaignId, command.publishOn, command.sendType, campaignService)
    }

    protected Map<String, Object> saveAndSendCampaign(KuorumUserSession user, CampaignRDTO campaignRDTO, Long campaignId, Date publishOn, String sendType, CampaignCreatorService campaignCreatorService) {
        CampaignRSDTO savedCampaign = null
        String msg
        if (sendType == CampaignContentCommand.CAMPAIGN_SEND_TYPE_SEND) {
            campaignRDTO.publishOn = Calendar.getInstance(user.timeZone).time
        } else {
            campaignRDTO.publishOn = TimeZoneUtil.convertToUserTimeZone(publishOn, user.timeZone)
        }

        if (campaignRDTO.publishOn) {
            // Published or Scheduled
            savedCampaign = campaignCreatorService.save(user, campaignRDTO, campaignId)

            Date date = new Date()
            Date after5minutes = new Date()

            // If Scheduled in the next 5 minutes, consider published
            use(TimeCategory) {
                after5minutes = date + 5.minutes
            }

            if (campaignRDTO.publishOn > after5minutes) {
                // Shceduled over 5 minutes
                msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                        savedCampaign.title,
                        g.formatDate(date: campaignRDTO.publishOn, type: "datetime", style: "SHORT")
                ])
            } else {
                // Published or scheduled within 5 minutes
                msg = g.message(code: 'tools.massMailing.saved.advise', args: [
                        savedCampaign.title,
                        g.formatDate(date: campaignRDTO.publishOn, type: "datetime", style: "SHORT")
                ])
            }
        } else {
            // Draft
            savedCampaign = campaignCreatorService.save(user, campaignRDTO, campaignId)
            msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [savedCampaign.title])
        }

        if (sendType == CampaignContentCommand.CAMPAIGN_SEND_TYPE_ACTIVATE) {
            campaignService.pauseCampaign(user, campaignId, false)
        }

        [msg: msg, campaign: savedCampaign, nextStep: processNextStep(user, savedCampaign, campaignRDTO.publishOn != null)]
    }

    protected def copyCampaign(Long campaignId, CampaignCreatorService campaignService) {
        KuorumUserSession loggedUser = springSecurityService.principal
        try {
            CampaignRSDTO campaignRSDTO = campaignService.copy(loggedUser, campaignId)
        } catch (KuorumException exception) {
            flash.error = message(code: exception.errors.first().code)
        } finally {
            redirect(mapping: 'politicianCampaigns')
        }
    }

    private def processNextStep(KuorumUserSession user, CampaignRSDTO campaignRSDTO, Boolean checkPaymentRedirect) {
        return [
                mapping: params.redirectLink,
                params : campaignRSDTO.encodeAsLinkProperties()
        ]
    }

    protected void removeCampaign(Long campaignId, CampaignCreatorService campaignService) {
        KuorumUserSession loggedUser = springSecurityService.principal
        campaignService.remove(loggedUser, campaignId)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def checkGroupCampaignValidation(String userAlias, Long campaignId) {
        String userId = null;
        if (springSecurityService.isLoggedIn()) {
            userId = springSecurityService.principal.id.toString();
        }
        Boolean belongsToCampaignGroup = campaignService.userBelongToCampaignGroup(userAlias, campaignId, userId)

        render(contentType: "application/json") {
            [success: true, belongsToCampaignGroup: belongsToCampaignGroup]
        }
    }

    protected boolean checkRequiredProfileData(CampaignRSDTO campaignRSDTO) {
        // TODO: May be this logic should be in the API
        KuorumUserSession loggedUser = springSecurityService.principal
        BasicDataKuorumUserRSDTO campaignOwner = campaignRSDTO.user
        ContactRSDTO ownerContact = contactService.getContactByEmail(campaignOwner.getId(), loggedUser.getEmail())
        KuorumUserRSDTO user = kuorumUserService.findUserRSDTO(loggedUser)
        return checkBasicData(user) &&
                checkPictures(user) &&
                checkFiles(campaignOwner, ownerContact);
    }

    private boolean checkBasicData(KuorumUserRSDTO loggedUser) {
        return loggedUser.nid && loggedUser.name && loggedUser.bio && loggedUser.nid && loggedUser.getPhone() && loggedUser.getPhonePrefix()
    }

    private boolean checkPictures(KuorumUserRSDTO user) {
        return user.getAvatarUrl()
    }

    private boolean checkFiles(BasicDataKuorumUserRSDTO campaignOwner, ContactRSDTO ownerContact) {
        List<String> files = contactService.getFiles(campaignOwner.getId().toString(), ownerContact)
        return files.size() >= WebConstants.MIN_FILES_PER_DOC_IN_CONTEST;
    }

    def campaignUserValidChecker() {
        Long campaignId = Long.parseLong(params.campaignId)
        String userUUID = cookieUUIDService.getUserUUID();
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.getUserValidationStatus(userUUID, campaignId)
        render([validated: userValidationRSDTO.isGranted(), success: true, pendingValidations: getPendingValidations(userValidationRSDTO)] as JSON)
    }
    // USER VALIDATION

    def domainUserValidChecker() {
        Long campaignId = Long.parseLong(params.campaignId)
        KuorumUserSession userSession = cookieUUIDService.buildAnonymousUser();
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.getUserValidationStatus(userSession, campaignId)
        render([validated: userValidationRSDTO.isGranted(), success: true, pendingValidations: getPendingValidations(userValidationRSDTO)] as JSON)
    }


    def validateUser(DomainValidationCommand domainValidationCommand) {
        if (domainValidationCommand.hasErrors()) {
            render([validated: false, success: false, msg: message(error: domainValidationCommand.getErrors().getAllErrors().get(0))] as JSON)
            return
        }
        Long campaignId = Long.parseLong(params.campaignId)
        KuorumUserSession userSession = springSecurityService.principal
        Evidences evidences = new HttpRequestRecoverEvidences(request);
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.userDomainValidation(userSession, evidences, campaignId, domainValidationCommand.ndi, domainValidationCommand.postalCode, domainValidationCommand.birthDate)
        render([
                success           : userValidationRSDTO.censusStatus.isGranted(),
                validated         : userValidationRSDTO.isGranted(),
                msg               : userValidationRSDTO.censusStatus.isGranted() ? "Success validation" : g.message(code: 'kuorum.web.commands.profile.DomainValidationCommand.validationError'),
                pendingValidations: getPendingValidations(userValidationRSDTO)
        ] as JSON)
    }

    def validateUserPhoneSendSMS(DomainUserPhoneValidationCommand domainUserPhoneValidationCommand) {
        KuorumUserSession votingUser = cookieUUIDService.buildAnonymousUser();
        Long campaignId = Long.parseLong(params.campaignId)
        try {
            UserPhoneValidationRDTO userPhoneValidationRDTO = kuorumUserService.sendSMSWithValidationCode(votingUser, campaignId, domainUserPhoneValidationCommand.phoneNumber.toString(), domainUserPhoneValidationCommand.phoneNumberPrefix)
            cookieUUIDService.buildAnonymousUser(userPhoneValidationRDTO.getUserId());
            render([
                    validated                  : userPhoneValidationRDTO.validationStatus.phoneStatus.isGranted(),
                    pendingValidations         : getPendingValidations(userPhoneValidationRDTO.validationStatus),
                    success                    : true,
                    hash                       : userPhoneValidationRDTO.getHash(),
                    validationPhoneNumberPrefix: userPhoneValidationRDTO.getPhoneNumberPrefix(),
                    validationPhoneNumber      : userPhoneValidationRDTO.getPhoneNumber()] as JSON)
        } catch (KuorumException e) {
            render([validated: false, success: false, hash: null, validationPhoneNumberPrefix: null, validationPhoneNumber: null, msg: g.message(code: 'kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumber.repeatedNumber')] as JSON)
        } catch (Exception e) {
            render([validated: false, success: false, hash: null, validationPhoneNumberPrefix: null, validationPhoneNumber: null, msg: 'Internal error. Try again or contact with info@kuorum.org'] as JSON)
        }
    }

    def validateUserPhone(DomainUserPhoneCodeValidationCommand domainUserPhoneValidationCommand) {
        if (domainUserPhoneValidationCommand.hasErrors()) {
            render([validated: false, success: false, msg: message(error: domainUserPhoneValidationCommand.getErrors().getAllErrors().get(0))] as JSON)
            return
        }
        Long campaignId = Long.parseLong(params.campaignId)
        KuorumUserSession userSession = cookieUUIDService.buildAnonymousUser();
        Evidences evidences = new HttpRequestRecoverEvidences(request);
        UserValidationRSDTO userValidationRSDTO = kuorumUserService.userPhoneDomainValidation(userSession, evidences, campaignId, domainUserPhoneValidationCommand.validationPhoneNumberPrefix, domainUserPhoneValidationCommand.validationPhoneNumber, domainUserPhoneValidationCommand.phoneHash, domainUserPhoneValidationCommand.phoneCode)
        render([
                success           : userValidationRSDTO.phoneStatus.isGranted(),
                validated         : userValidationRSDTO.isGranted(),
                msg               : userValidationRSDTO.phoneStatus.isGranted() ? "Success validation" : g.message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.validationError'),
                pendingValidations: getPendingValidations(userValidationRSDTO)
        ] as JSON)
    }

    def validateUserCustomCode(DomainUserCustomCodeValidationCommand domainUserCustomCodeValidationCommand) {
        if (domainUserCustomCodeValidationCommand.hasErrors()) {
            render([validated: false, success: false, msg: message(error: domainUserCustomCodeValidationCommand.getErrors().getAllErrors().get(0))] as JSON)
            return
        }
        KuorumUserSession userSession = cookieUUIDService.buildAnonymousUser();
        Long campaignId = Long.parseLong(params.campaignId)
        String msg
        UserValidationRSDTO userValidationRSDTO = null
        Evidences evidences = new HttpRequestRecoverEvidences(request);
        userValidationRSDTO = kuorumUserService.userCodeDomainValidation(userSession, evidences, campaignId, domainUserCustomCodeValidationCommand.customCode)
        if (userValidationRSDTO.codeStatus.isGranted()) {
            msg = "Success validation"
        } else {
            msg = g.message(code: "kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.customCode.validationError", args: [userSession.email.replaceFirst(/([^@]{3}).*@(..).*/, "\$1***@\$2***")])
            log.info("Error validating user: ${msg}")
        }

        render([
                success           : userValidationRSDTO.codeStatus.isGranted(),
                validated         : userValidationRSDTO.isGranted(),
                msg               : msg,
                pendingValidations: getPendingValidations(userValidationRSDTO)
        ] as JSON)
    }


    private def getPendingValidations(UserValidationRSDTO userValidationRSDTO) {
        String phone = null;
        Boolean predefinedPhone = false;
        if (!userValidationRSDTO.phoneStatus.isGranted()) {
            if (springSecurityService.isLoggedIn()) {
                KuorumUserSession userSession = cookieUUIDService.buildAnonymousUser();
                KuorumUserExtraDataRSDTO extraDataRSDTO = kuorumUserService.findUserExtendedDataRSDTO(userSession)
                phone = extraDataRSDTO.phoneNumber?.encodeAsHiddenPhone()
                predefinedPhone = extraDataRSDTO.phoneNumber ? true : false;
            } else {
                predefinedPhone = false;
            }
        }
        return [
                tokenMailValidation: [success: userValidationRSDTO.tokenMailStatus.isGranted(), data: [:]],
                censusValidation   : [success: userValidationRSDTO.censusStatus.isGranted(), data: [:]],
                phoneValidation    : [success: userValidationRSDTO.phoneStatus.isGranted(), data: [phone: phone, predefinedPhone: predefinedPhone]],
                codeValidation     : [success: userValidationRSDTO.codeStatus.isGranted(), data: [:]]

        ]
    }

}
