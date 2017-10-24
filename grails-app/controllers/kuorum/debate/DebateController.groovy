package kuorum.debate

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.files.FileService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.util.TimeZoneUtil
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.debate.DebateContentCommand
import kuorum.web.commands.payment.debate.DebateSettingsCommand
import org.bson.types.ObjectId
import org.kuorum.rest.model.communication.debate.DebateRDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.search.ProposalPageRSDTO
import org.kuorum.rest.model.communication.debate.search.SearchProposalRSDTO
import org.kuorum.rest.model.communication.debate.search.SortProposalRDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.CustomerService
import payment.campaign.DebateService
import payment.campaign.ProposalService
import payment.contact.ContactService

import javax.servlet.http.HttpServletResponse

class DebateController {

    def springSecurityService

    KuorumUserService kuorumUserService
    FileService fileService
    ContactService contactService
    DebateService debateService
    ProposalService proposalService
    CookieUUIDService cookieUUIDService
    CustomerService customerService

    def show() {
        String viewerId = cookieUUIDService.buildUserUUID()
        try {
            DebateRSDTO debate = debateService.findDebate( params.userAlias, Long.parseLong((String) params.debateId),viewerId)
            if (!debate) {
                throw new KuorumException(message(code: "debate.notFound") as String)
            }

            KuorumUser debateUser = KuorumUser.get(new ObjectId(debate.user.id));
            SearchProposalRSDTO searchProposalRSDTO = new SearchProposalRSDTO()
            searchProposalRSDTO.sort = new SortProposalRDTO()
            searchProposalRSDTO.sort.direction = SortProposalRDTO.Direction.DESC
            searchProposalRSDTO.sort.field = SortProposalRDTO.Field.LIKES
            searchProposalRSDTO.size = Integer.MAX_VALUE // Sorting and filtering will be done using JS. We expect maximum 100 proposals

            ProposalPageRSDTO proposalPage = proposalService.findProposal(debate, searchProposalRSDTO,viewerId)
            List<KuorumUser> pinnedUsers = proposalPage.data.findAll{it.pinned}.collect{KuorumUser.get(new ObjectId(it.debateUser.id))}.findAll{it}.unique()
            def model = [debate: debate, debateUser: debateUser, proposalPage:proposalPage, pinnedUsers:pinnedUsers];
            if (params.printAsWidget){
                render view: 'widgetDebate', model: model
            }else{
                return model;
            }
        } catch (Exception ignored) {
            flash.error = message(code: "debate.notFound")
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return debateModelSettings(new DebateSettingsCommand(), null)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editSettingsStep(){
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.findDebate( user, Long.parseLong((String) params.debateId), viewerUid)

        return debateModelSettings(new DebateSettingsCommand(), debateRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editContentStep(){
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.findDebate( user, Long.parseLong((String) params.debateId), viewerUid)
        setDebateAsDraft(user, debateRSDTO)
        return debateModelContent(new DebateContentCommand(), debateRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveSettings(DebateSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: debateModelSettings(command, null)
            return
        }

        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilterSettings(params, command)
        Long debateId = params.debateId?Long.parseLong(params.debateId):null
        Map<String, Object> resultDebate = saveAndSendDebateSettings(user, command, debateId, anonymousFilter)

        //flash.message = resultDebate.msg.toString()

        redirect mapping: nextStep, params: resultDebate.debate.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveContent(DebateContentCommand command) {
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContentStep', model: debateModelContent(command, null)
            return
        }

        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long debateId = params.debateId?Long.parseLong(params.debateId):null
        Map<String, Object> resultDebate = saveAndSendDebateContent(user, command, debateId)
        if (resultDebate.goToPaymentProcess){
            String paymentRedirect = g.createLink(mapping:"debateEditContent", params:resultDebate.debate.encodeAsLinkProperties() )
            cookieUUIDService.setPaymentRedirect(paymentRedirect)
            redirect(mapping: "paymentStart")
        }else {
            //flash.message = resultDebate.msg.toString()
            redirect mapping: nextStep, params: resultDebate.debate.encodeAsLinkProperties()
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def remove(Long debateId) {
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        debateService.removeDebate(loggedUser, debateId)
        render ([msg: "Debate deleted"] as JSON)
    }

    private def debateModelSettings(DebateSettingsCommand command, DebateRSDTO debateRSDTO) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        if(debateRSDTO) {
            command.campaignName = debateRSDTO.name
            command.tags = debateRSDTO.triggeredTags
            command.filterId = debateRSDTO.newsletter?.filter?.id
            if(debateRSDTO.newsletter.filter && !filters.find{it.id == debateRSDTO.newsletter.filter.id}){
                ExtendedFilterRSDTO anonymousFilter = contactService.getFilter(user, debateRSDTO.newsletter.filter.id)
                filters.add(anonymousFilter)
            }
        }

        [
                filters: filters,
                command: command,
                totalContacts: contactPageRSDTO.total,
                debate: debateRSDTO
        ]
    }

    private def debateModelContent(DebateContentCommand command, DebateRSDTO debateRSDTO) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        Long debateId = params.debateId?Long.parseLong(params.debateId):null
        if(!debateRSDTO){
            debateRSDTO = debateService.findDebate(user, debateId)
        }

        command.title = debateRSDTO.title
        command.body = debateRSDTO.body
        command.videoPost = debateRSDTO.videoUrl

        if(debateRSDTO.datePublished){
            command.publishOn = debateRSDTO.datePublished
        }

        if (debateRSDTO.photoUrl) {
            KuorumFile kuorumFile = KuorumFile.findByUrl(debateRSDTO.photoUrl)
            command.headerPictureId = kuorumFile?.id
        }
        Long numberRecipients = debateRSDTO.newsletter?.filter?.amountOfContacts!=null?
                debateRSDTO.newsletter?.filter?.amountOfContacts:
                contactService.getUsers(user, null).total;
        [
                filters: filters,
                command: command,
                numberRecipients: numberRecipients,
                debate: debateRSDTO,
                status: debateRSDTO.campaignStatusRSDTO
        ]
    }

    private FilterRDTO recoverAnonymousFilterSettings(params, DebateSettingsCommand command) {
        ContactFilterCommand filterCommand = (ContactFilterCommand) bindData(new ContactFilterCommand(), params)
        contactService.transformCommand(filterCommand, "Custom filter for ${command.campaignName}")
    }

    private Map<String, Object> saveAndSendDebateSettings(KuorumUser user, DebateSettingsCommand command,
                                                  Long debateId = null, FilterRDTO anonymousFilter = null) {

        DebateRDTO debateRDTO = convertCommandSettingsToDebate(command, user, anonymousFilter, debateId)
        DebateRSDTO savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
        String msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedDebate.title])

        [msg: msg, debate: savedDebate]
    }

    private Map<String, Object> saveAndSendDebateContent(KuorumUser user, DebateContentCommand command,
                                                  Long debateId = null) {
        DebateRDTO debateRDTO = convertCommandContentToDebate(command, user, debateId)

        Boolean validSubscription = customerService.validSubscription(user);
        Boolean goToPaymentProcess = !validSubscription && command.publishOn;

        String msg
        DebateRSDTO savedDebate
        if (command.publishOn) {
            savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
            if (command.publishOn < new Date()) {
                msg = g.message(code: 'tools.massMailing.saved.advise', args: [
                        savedDebate.title
                ])
            } else {
                msg = g.message(code: 'tools.massMailing.schedule.advise', args: [
                        savedDebate.title,
                        g.formatDate(date: command.publishOn, type: "datetime", style: "SHORT")
                ])
            }
        } else {
            // IS A DRAFT
            savedDebate = debateService.saveDebate(user, debateRDTO, debateId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedDebate.title])
        }

        [msg: msg, debate: savedDebate,goToPaymentProcess:goToPaymentProcess]
    }

    private DebateRDTO convertCommandSettingsToDebate(DebateSettingsCommand command, KuorumUser user, FilterRDTO anonymousFilter, Long debateId) {
        DebateRDTO debateRDTO = createDebateRDTO(user, debateId)
        debateRDTO.name = command.campaignName
        debateRDTO.setTriggeredTags(command.tags)
        if (command.filterEdited) {
            //anonymousFilter.setName(g.message(code:'tools.contact.filter.anonymousName', args: anonymousFilter.getName()))
            debateRDTO.setAnonymousFilter(anonymousFilter)
            debateRDTO.setFilterId(null)
        } else {
            debateRDTO.setFilterId(command.filterId)
        }
        debateRDTO
    }

    private DebateRDTO convertCommandContentToDebate(DebateContentCommand command, KuorumUser user, Long debateId) {
        DebateRDTO debateRDTO = createDebateRDTO(user, debateId)
        debateRDTO.title = command.title
        debateRDTO.body = command.body
        if(command.sendType == 'SEND'){
            debateRDTO.publishOn = Calendar.getInstance(user.getTimeZone()).time;
        }
        else{
            debateRDTO.publishOn = TimeZoneUtil.convertToUserTimeZone(command.publishOn, user.timeZone)
        }

        // Multimedia URL
        if (command.fileType == FileType.IMAGE.toString() && command.headerPictureId) {
            // Save image
            KuorumFile picture = KuorumFile.get(command.headerPictureId)
            picture = fileService.convertTemporalToFinalFile(picture)
            fileService.deleteTemporalFiles(user)
            debateRDTO.setPhotoUrl(picture.getUrl())

            // Remove video
            debateRDTO.setVideoUrl(null)
        } else if (command.fileType == FileType.YOUTUBE.toString() && command.videoPost) {
            // Save video
            KuorumFile urlYoutubeFile = fileService.createYoutubeKuorumFile(command.videoPost, user)
            debateRDTO.setVideoUrl(urlYoutubeFile?.url)

            // Remove image
            if (command.headerPictureId) {
                KuorumFile picture = KuorumFile.get(command.headerPictureId)
                fileService.deleteKuorumFile(picture)
                command.setHeaderPictureId(null)
                debateRDTO.setPhotoUrl(null)
            }
        }

        debateRDTO
    }

    private DebateRDTO createDebateRDTO(KuorumUser user, Long debateId){
        DebateRDTO debateRDTO = new DebateRDTO();
        if(debateId){
            DebateRSDTO debateRSDTO = debateService.findDebate(user, debateId)
            populateDebate(debateRDTO, debateRSDTO)
        }
        return debateRDTO
    }

    private void populateDebate(DebateRDTO debateRDTO, DebateRSDTO debateRSDTO){
        debateRDTO.name = debateRSDTO.name
        debateRDTO.triggeredTags = debateRSDTO.triggeredTags
        debateRDTO.anonymousFilter = debateRDTO.anonymousFilter
        debateRDTO.filterId = debateRSDTO.newsletter?.filter?.id
        debateRDTO.photoUrl = debateRSDTO.photoUrl
        debateRDTO.videoUrl = debateRSDTO.videoUrl
        debateRDTO.title = debateRSDTO.title
        debateRDTO.body = debateRSDTO.body
        debateRDTO.publishOn = debateRSDTO.datePublished
    }

    private void setDebateAsDraft(KuorumUser user, DebateRSDTO debate){
        if (debate.newsletter.status == CampaignStatusRSDTO.SCHEDULED){
            DebateRDTO debateRDTO = new DebateRDTO();
            populateDebate(debateRDTO , debate)
            debateRDTO.setPublishOn(null)
            debateService.saveDebate(user, debateRDTO, debate.getId())
        }
    }
}
