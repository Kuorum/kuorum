package kuorum.debate

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.campaign.Event
import kuorum.campaign.EventRegistration
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.files.FileService
import kuorum.politician.CampaignController
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.util.TimeZoneUtil
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.debate.DebateContentCommand
import org.bson.types.ObjectId
import org.kuorum.rest.model.communication.CampaignRDTO
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
import payment.campaign.ProposalService

import javax.servlet.http.HttpServletResponse

class DebateController extends CampaignController{


    KuorumUserService kuorumUserService
    FileService fileService
    ProposalService proposalService
    CookieUUIDService cookieUUIDService
    CustomerService customerService

    def show() {
        String viewerId = cookieUUIDService.buildUserUUID()
        try {
            DebateRSDTO debate = debateService.find( params.userAlias, Long.parseLong((String) params.debateId),viewerId)
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
//            if (lastActivity){
//                lastModified(debate.lastActivity)
//            }
            List<KuorumUser> pinnedUsers = proposalPage.data
                    .findAll{it.pinned}
                    .collect{KuorumUser.get(new ObjectId(it.user.id))}
                    .findAll{it}
                    .unique()
                    .sort{ ku1, ku2 -> ku1.avatar != null?-1:ku2.avatar!=null?1:0 }

            def model = [debate: debate, debateUser: debateUser, proposalPage:proposalPage, pinnedUsers:pinnedUsers];

            // BORRAR - SOLO PARA TOLEDO
            if (debateUser.id.toString().equals("5a0056bfa9aa0c5bb6a69fae")){
                model.put("eventData", Event.findByDebateId(debate.id))
                if (springSecurityService.isLoggedIn()){
                    KuorumUser userLogged = springSecurityService.currentUser
                    model.put("eventRegistration", EventRegistration.findByDebateIdAndUserId(debate.id, userLogged.id))
                }
            }

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
        return debateModelSettings(new CampaignSettingsCommand(debatable:true), null)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editSettingsStep(){
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.find( user, Long.parseLong((String) params.debateId), viewerUid)

        return debateModelSettings(new CampaignSettingsCommand(debatable:true), debateRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editContentStep(){
        String viewerUid = cookieUUIDService.buildUserUUID()
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.find( user, Long.parseLong((String) params.debateId), viewerUid)
        setDebateAsDraft(user, debateRSDTO)
        return debateModelContent(new DebateContentCommand(), debateRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: debateModelSettings(command, null)
            return
        }

        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        FilterRDTO anonymousFilter = recoverAnonymousFilterSettings(params, command)
        Long debateId = params.debateId?Long.parseLong(params.debateId):null
        Map<String, Object> result = saveCampaignSettings(user, command, debateId, anonymousFilter, debateService)

        //flash.message = resultDebate.msg.toString()

        redirect mapping: nextStep, params: result.campaign.encodeAsLinkProperties()
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

    private def debateModelSettings(CampaignSettingsCommand command, DebateRSDTO debateRSDTO) {
        def model = modelSettings(command, debateRSDTO)
        command.debatable=true
        return model
    }

    private def debateModelContent(DebateContentCommand command, DebateRSDTO debateRSDTO) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)
        ContactPageRSDTO contactPageRSDTO = contactService.getUsers(user)

        Long debateId = params.debateId?Long.parseLong(params.debateId):null
        if(!debateRSDTO){
            debateRSDTO = debateService.find(user, debateId)
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

    def sendReport(Long debateId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        debateService.sendReport(loggedUser, debateId)
        Boolean isAjax = request.xhr
        if(isAjax){
            render ([success:"success"] as JSON)
        } else{
            flash.message = g.message(code: 'modal.exportedTrackingEvents.title')
            redirect (mapping: 'politicianDebateStatsShow', params:[debateId: debateId])
        }
    }

    private Map<String, Object> saveAndSendDebateContent(KuorumUser user, DebateContentCommand command,
                                                  Long debateId = null) {
        DebateRDTO debateRDTO = convertCommandContentToDebate(command, user, debateId)

        Boolean validSubscription = customerService.validSubscription(user);
        Boolean goToPaymentProcess = !validSubscription && command.publishOn;

        String msg
        DebateRSDTO savedDebate
        if (command.publishOn) {
            savedDebate = debateService.save(user, debateRDTO, debateId)
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
            savedDebate = debateService.save(user, debateRDTO, debateId)
            msg = g.message(code:'tools.massMailing.saveDraft.advise', args: [savedDebate.title])
        }

        [msg: msg, debate: savedDebate,goToPaymentProcess:goToPaymentProcess]
    }

    private DebateRDTO convertCommandContentToDebate(DebateContentCommand command, KuorumUser user, Long debateId) {
        DebateRDTO debateRDTO = createRDTO(user, debateId, debateService)
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

    private void setDebateAsDraft(KuorumUser user, DebateRSDTO debate){
        if (debate.newsletter.status == CampaignStatusRSDTO.SCHEDULED){
            DebateRDTO debateRDTO = debateService.map(debate)
            debateRDTO.setPublishOn(null)
            debateService.save(user, debateRDTO, debate.getId())
        }
    }
}
