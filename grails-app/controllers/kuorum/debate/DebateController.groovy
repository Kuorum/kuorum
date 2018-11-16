package kuorum.debate

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import org.kuorum.rest.model.communication.debate.DebateRSDTO

class DebateController extends CampaignController{

    @Secured(['ROLE_CAMPAIGN_DEBATE'])
    def create() {
        return debateModelSettings(new CampaignSettingsCommand(debatable:true), null)
    }

    @Secured(['ROLE_CAMPAIGN_DEBATE','ROLE_CAMPAIGN_EVENT'])
    def editSettingsStep(){
        KuorumUserSession user = springSecurityService.principal
        DebateRSDTO debateRSDTO = debateService.find( user, Long.parseLong((String) params.campaignId))

        return debateModelSettings(new CampaignSettingsCommand(debatable:true), debateRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_DEBATE','ROLE_CAMPAIGN_EVENT'])
    def editContentStep(){
        Long campaignId = Long.parseLong((String) params.campaignId)
        DebateRSDTO debateRSDTO = setCampaignAsDraft(campaignId, debateService)
        return campaignModelContent(campaignId, debateRSDTO, null, debateService)
    }

    @Secured(['ROLE_CAMPAIGN_DEBATE','ROLE_CAMPAIGN_EVENT'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: debateModelSettings(command, null)
            return
        }

        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(command, params, debateService)

        //flash.message = resultDebate.msg.toString()
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_DEBATE','ROLE_CAMPAIGN_EVENT'])
    def saveContent(CampaignContentCommand command) {
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(campaignId, null,command, debateService)
            return
        }

        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, debateService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_DEBATE','ROLE_CAMPAIGN_EVENT'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, debateService)
        render ([msg: "Debate deleted"] as JSON)
    }

    private def debateModelSettings(CampaignSettingsCommand command, DebateRSDTO debateRSDTO) {
        def model = modelSettings(command, debateRSDTO)
        command.debatable=true
        model.options =[debatable:debateRSDTO?.event?true:false, endDate:false]
        return model
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def sendReport(Long campaignId){
        KuorumUserSession loggedUser = springSecurityService.principal
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
