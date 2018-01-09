package kuorum.debate

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.campaign.Event
import kuorum.campaign.EventRegistration
import kuorum.core.exception.KuorumException
import kuorum.politician.CampaignController
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import org.bson.types.ObjectId
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.search.ProposalPageRSDTO
import org.kuorum.rest.model.communication.debate.search.SearchProposalRSDTO
import org.kuorum.rest.model.communication.debate.search.SortProposalRDTO
import org.kuorum.rest.model.communication.event.EventRegistrationRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import payment.campaign.ProposalService
import payment.campaign.event.EventService

import javax.servlet.http.HttpServletResponse

class DebateController extends CampaignController{


    KuorumUserService kuorumUserService
    ProposalService proposalService
    CookieUUIDService cookieUUIDService

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
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.find( user, Long.parseLong((String) params.debateId))

        return debateModelSettings(new CampaignSettingsCommand(debatable:true), debateRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editContentStep(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long debateId = Long.parseLong((String) params.debateId);
        DebateRSDTO debateRSDTO = setCampaignAsDraft(user, debateId, debateService)
        return campaignModelContent(debateId, debateRSDTO, null, debateService)
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
        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(user, command, debateId, anonymousFilter, debateService)

        //flash.message = resultDebate.msg.toString()

        redirect mapping: nextStep, params: result.campaign.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveContent(CampaignContentCommand command) {
        Long debateId = params.debateId?Long.parseLong(params.debateId):null
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(debateId, null,command, debateService)
            return
        }

        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Map<String, Object> resultDebate = saveAndSendCampaignContent(user, command, debateId, debateService)
        if (resultDebate.goToPaymentProcess){
            String paymentRedirect = g.createLink(mapping:"debateEditContent", params:resultDebate.campaign.encodeAsLinkProperties() )
            cookieUUIDService.setPaymentRedirect(paymentRedirect)
            redirect(mapping: "paymentStart")
        }else {
            //flash.message = resultDebate.msg.toString()
            redirect mapping: nextStep, params: resultDebate.campaign.encodeAsLinkProperties()
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
}
