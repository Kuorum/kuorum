package kuorum.politician

import grails.plugin.springsecurity.SpringSecurityService
import groovy.time.TimeCategory
import kuorum.KuorumFile
import kuorum.core.FileType
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.files.FileService
import kuorum.register.KuorumUserSession
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.util.TimeZoneUtil
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.contact.ContactFilterCommand
import org.bson.types.ObjectId
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.CampaignTypeRSDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.event.EventRDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.CustomerService
import payment.campaign.*
import payment.contact.ContactService

import javax.servlet.http.HttpServletResponse

class CampaignController {

    PostService postService
    DebateService debateService
    SurveyService surveyService
    ContactService contactService
    ParticipatoryBudgetService participatoryBudgetService
    DistrictProposalService districtProposalService
    PetitionService petitionService
    SpringSecurityService springSecurityService
    FileService fileService
    CustomerService customerService

    KuorumUserService kuorumUserService
    CookieUUIDService cookieUUIDService
    CampaignService campaignService

    def show() {
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
        try{
            CampaignRSDTO campaignRSDTO = campaignService.find(user, Long.parseLong(params.campaignId),viewerUid)
            if (!campaignRSDTO) {
                throw new KuorumException(message(code: "post.notFound") as String)
            }
            def dataView = [view:null, model:null]
            switch (campaignRSDTO.campaignType){
                case CampaignTypeRSDTO.DEBATE:
                    dataView = debateService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                case CampaignTypeRSDTO.POST:
                    dataView = postService.buildView(campaignRSDTO, user, viewerUid, params)
                    break
                case CampaignTypeRSDTO.SURVEY:
                    dataView = surveyService.buildView(campaignRSDTO, user, viewerUid, params)
                    break;
                case CampaignTypeRSDTO.PARTICIPATORY_BUDGET:
                    dataView = participatoryBudgetService.buildView(campaignRSDTO, user, viewerUid, params)
                    break;
                case CampaignTypeRSDTO.DISTRICT_PROPOSAL:
                    dataView = districtProposalService.buildView(campaignRSDTO, user, viewerUid, params)
                    break;
                case CampaignTypeRSDTO.PETITION:
                    dataView = petitionService.buildView(campaignRSDTO, user, viewerUid, params)
                    break;
                default:
                    log.error("Campaign type not recognized: ${campaignRSDTO.campaignType}")
                    throw new Exception("Campaign type not recognized: ${campaignRSDTO.campaignType}")
            }
            render view: dataView.view, model:dataView.model
        }catch (Exception ignored){
            flash.error = message(code: "post.notFound")
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
    }

    def findLiUserCampaigns(String userId){
        KuorumUser user = KuorumUser.get(new ObjectId(userId));
        List<CampaignRSDTO> campaigns = campaignService.findAllCampaigns(user).findAll{it.newsletter.status==CampaignStatusRSDTO.SENT};
        render template: '/campaigns/cards/campaignsList', model: [campaigns:campaigns, showAuthor:true]

    }

    protected def modelSettings(CampaignSettingsCommand command, CampaignRSDTO campaignRSDTO = null) {
        KuorumUserSession user = springSecurityService.principal
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        if(campaignRSDTO){
            command.campaignName = campaignRSDTO.name
            command.tags = campaignRSDTO.triggeredTags
            command.filterId = campaignRSDTO.newsletter?.filter?.id
            command.endDate = campaignRSDTO.endDate
            command.checkValidation = campaignRSDTO.checkValidation
            if (campaignRSDTO.hasProperty('causes')){
                command.causes = campaignRSDTO.causes
            }
            if(campaignRSDTO.newsletter.filter && !filters.find{it.id==campaignRSDTO.newsletter.filter.id}){
                ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(user, campaignRSDTO.newsletter.filter.id)
                filters.add(anonymousFilter)
            }
        }else{
            command.checkValidation = CustomDomainResolver.domainRSDTO?.validation
        }
        [
                filters: filters,
                command: command,
                totalContacts: contactPageRSDTO.total,
                campaign: campaignRSDTO,
                status: campaignRSDTO?.newsletter?.status?:null,
                domainValidation:CustomDomainResolver.domainRSDTO?.validation
        ]
    }

    protected FilterRDTO recoverAnonymousFilterSettings(params, CampaignSettingsCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        contactService.transformCommand(filterCommand, "Custom filter for ${command.campaignName}")
    }

    protected mapCommandSettingsToRDTO(KuorumUserSession user, CampaignRDTO rdto, CampaignSettingsCommand command, FilterRDTO anonymousFilter){
        rdto.name = command.campaignName
        rdto.setTriggeredTags(command.tags)
        rdto.causes = command.causes
        rdto.endDate = TimeZoneUtil.convertToUserTimeZone(command.endDate, user.timeZone)
        if (CustomDomainResolver.domainRSDTO?.validation){
            // Only if domain validation is active, then the checkValidation of the campaign is editable
            rdto.checkValidation = command.checkValidation?:false;
        }
        if (command.filterEdited) {
            //anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            rdto.setAnonymousFilter(anonymousFilter)
            rdto.setFilterId(null)
        } else {
            rdto.setFilterId(command.filterId)
        }
        if (command.eventAttached && !rdto.event){
            rdto.event = new EventRDTO();
        }
        rdto
    }

    protected CampaignRDTO createRDTO(KuorumUserSession user, Long campaignId, CampaignCreatorService campaignService){
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
            CampaignCreatorService campaignService){
        KuorumUserSession user = springSecurityService.principal
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        FilterRDTO anonymousFilter = recoverAnonymousFilterSettings(params, command)

        CampaignRDTO campaignRDTO = convertCommandSettingsToRDTO(command, user, anonymousFilter, campaignId, campaignService)
        CampaignRSDTO campaignSaved = campaignService.save(user, campaignRDTO, campaignId)
        String msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [campaignSaved.title])

        def nextStep =[
                mapping:params.redirectLink,
                params:campaignSaved.encodeAsLinkProperties()
        ]

        [msg: msg, campaign: campaignSaved,nextStep:nextStep]
    }

    protected CampaignRSDTO setCampaignAsDraft(Long campaignId, CampaignCreatorService campaignService){
        KuorumUserSession user = springSecurityService.principal
        CampaignRSDTO campaignRSDTO = campaignService.find(user, campaignId)
        if (campaignRSDTO && campaignRSDTO.newsletter.status == CampaignStatusRSDTO.SCHEDULED){
            CampaignRDTO campaignRDTO = campaignService.map(campaignRSDTO)
            campaignRDTO.setPublishOn(null)
            campaignService.save(user, campaignRDTO, campaignId)
        }
        return campaignRSDTO;

    }

    protected def campaignModelContent(Long campaignId, CampaignRSDTO campaignRSDTO=null, CampaignContentCommand command=null, CampaignCreatorService campaignService) {

        KuorumUserSession user = springSecurityService.principal
        if(!campaignRSDTO && campaignId){
            campaignRSDTO = campaignService.find(user, campaignId)
        }

        if (campaignRSDTO?.event && !campaignRSDTO.event.latitude){
            // Debate has an event attached but is not defined the place.
            // Redirects to edit event
            flash.message=g.message(code: 'tools.massMailing.event.advise.empty')
            String mapping = campaignRSDTO instanceof DebateRSDTO ? 'debateEditEvent':'postEditEvent'
            redirect mapping: mapping, params: campaignRSDTO.encodeAsLinkProperties()
            return
        }

        if (!command){
            command = new CampaignContentCommand();
            if (campaignRSDTO){
                command.title = campaignRSDTO.title
                command.body = campaignRSDTO.body
                if (campaignRSDTO.videoUrl){
                    command.videoPost = campaignRSDTO.videoUrl
                    command.fileType = FileType.YOUTUBE.toString()
                }

                if(campaignRSDTO.datePublished){
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
                command: command,
                numberRecipients: numberRecipients,
                campaign: campaignRSDTO,
                status: campaignRSDTO?.campaignStatusRSDTO?:CampaignStatusRSDTO.DRAFT
        ]
    }

    protected Long getCampaignNumberRecipients(KuorumUserSession user, CampaignRSDTO campaignRSDTO){
        Long numberRecipients = campaignRSDTO?.newsletter?.filter?.amountOfContacts!=null?
                campaignRSDTO.newsletter?.filter?.amountOfContacts:
                contactService.getUsers(user, null).total;
        return numberRecipients
    }

    protected CampaignRDTO convertCommandContentToRDTO(CampaignContentCommand command, KuorumUserSession user, Long campaignId, CampaignCreatorService campaignService) {
        CampaignRDTO campaignRDTO = createRDTO(user, campaignId, campaignService)
        campaignRDTO.title = command.title
        campaignRDTO.body = command.body

        // Multimedia URL
        if (command.fileType == FileType.IMAGE.toString() && command.headerPictureId) {
            // Save image
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            campaignRDTO.setPhotoUrl(picture.getUrl())

            // Remove video
            campaignRDTO.setVideoUrl(null)
        } else if (command.fileType == FileType.YOUTUBE.toString() && command.videoPost) {
            // Save video
            KuorumFile urlYoutubeFile = fileService.createYoutubeKuorumFile(command.videoPost, user)
            campaignRDTO.setVideoUrl(urlYoutubeFile?.url)

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

    protected Map<String, Object> saveAndSendCampaign(KuorumUserSession user, CampaignRDTO campaignRDTO, Long campaignId,Date publishOn,String sendType, CampaignCreatorService campaignService){
        CampaignRSDTO savedCampaign = null
        String msg
        if(sendType == 'SEND'){
            campaignRDTO.publishOn = Calendar.getInstance(user.timeZone).time;
        }
        else{
            campaignRDTO.publishOn = TimeZoneUtil.convertToUserTimeZone(publishOn, user.timeZone)
        }

        if(campaignRDTO.publishOn){
            // Published or Scheduled
            savedCampaign = campaignService.save(user, campaignRDTO, campaignId)

            Date date = new Date()
            Date after5minutes = new Date()

            // If Scheduled in the next 5 minutes, consider published
            use (TimeCategory){
                after5minutes = date + 5.minutes
            }

            if(campaignRDTO.publishOn > after5minutes){
                // Shceduled over 5 minutes
                msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                        savedCampaign.title,
                        g.formatDate(date: campaignRDTO.publishOn, type: "datetime", style: "SHORT")
                ])
            }
            else {
                // Published or scheduled within 5 minutes
                msg = g.message(code: 'tools.massMailing.saved.advise', args: [
                        savedCampaign.title,
                        g.formatDate(date: campaignRDTO.publishOn, type: "datetime", style: "SHORT")
                ])
            }
        }
        else {
            // Draft
            savedCampaign = campaignService.save(user, campaignRDTO, campaignId)
            msg = g.message(code: 'tools.massMailing.saveDraft.advise', args: [savedCampaign.title])
        }

        [msg: msg, campaign: savedCampaign, nextStep:processNextStep(user, savedCampaign, campaignRDTO.publishOn!= null)]
    }

    private def processNextStep(KuorumUserSession user, CampaignRSDTO campaignRSDTO, Boolean checkPaymentRedirect){
        return [
                mapping:params.redirectLink,
                params:campaignRSDTO.encodeAsLinkProperties()
        ]
    }

    protected void removeCampaign(Long campaignId, CampaignCreatorService campaignService){
        KuorumUserSession loggedUser = springSecurityService.principal
        campaignService.remove(loggedUser, campaignId)
    }
}
