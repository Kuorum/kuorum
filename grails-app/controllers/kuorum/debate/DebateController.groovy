package kuorum.debate

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO

class DebateController extends CampaignController{


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return debateModelSettings(new CampaignSettingsCommand(debatable:true), null)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editSettingsStep(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DebateRSDTO debateRSDTO = debateService.find( user, Long.parseLong((String) params.campaignId))

        return debateModelSettings(new CampaignSettingsCommand(debatable:true), debateRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editContentStep(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long campaignId = Long.parseLong((String) params.campaignId);
        DebateRSDTO debateRSDTO = setCampaignAsDraft(user, campaignId, debateService)
        return campaignModelContent(campaignId, debateRSDTO, null, debateService)
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
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(user, command, campaignId, anonymousFilter, debateService)

        //flash.message = resultDebate.msg.toString()

        redirect mapping: nextStep, params: result.campaign.encodeAsLinkProperties()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveContent(CampaignContentCommand command) {
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(campaignId, null,command, debateService)
            return
        }

        String nextStep = params.redirectLink
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Map<String, Object> resultDebate = saveAndSendCampaignContent(user, command, campaignId, debateService)
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
    def remove(Long campaignId) {
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        debateService.removeDebate(loggedUser, campaignId)
        render ([msg: "Debate deleted"] as JSON)
    }

    private def debateModelSettings(CampaignSettingsCommand command, DebateRSDTO debateRSDTO) {
        def model = modelSettings(command, debateRSDTO)
        command.debatable=true
        return model
    }

    def sendReport(Long campaignId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        debateService.sendReport(loggedUser, campaignId)
        Boolean isAjax = request.xhr
        if(isAjax){
            render ([success:"success"] as JSON)
        } else{
            flash.message = g.message(code: 'modal.exportedTrackingEvents.title')
            redirect (mapping: 'politicianCampaignStatsShow', params:[campaignId: campaignId])
        }
    }
}
