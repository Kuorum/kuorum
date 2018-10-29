package kuorum.petition

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.petition.SignPetitionCommand
import org.kuorum.rest.model.communication.petition.PetitionRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO

class PetitionController extends CampaignController{

    @Secured(['ROLE_PETITION','ROLE_ADMIN'])
    def create() {
        return petitionModelSettings(new CampaignSettingsCommand(debatable:false), null)
    }

    @Secured(['ROLE_PETITION', 'ROLE_ADMIN'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, petitionService)
        render ([msg: "Post deleted"] as JSON)
    }

    @Secured(['ROLE_PETITION','ROLE_ADMIN'])
    def editSettingsStep(){
        KuorumUser petitionUser = KuorumUser.get(springSecurityService.principal.id)
        PetitionRSDTO petitionRSDTO = petitionService.find(petitionUser, Long.parseLong(params.campaignId))
        return petitionModelSettings(new CampaignSettingsCommand(debatable:false), petitionRSDTO)

    }

    @Secured(['ROLE_PETITION','ROLE_ADMIN'])
    def editContentStep(){
        Long campaignId = Long.parseLong(params.campaignId)
        PetitionRSDTO petitionRSDTO = setCampaignAsDraft(campaignId, petitionService)
        return campaignModelContent(campaignId, petitionRSDTO, null, petitionService)
    }

    @Secured(['ROLE_PETITION','ROLE_ADMIN'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: petitionModelSettings(command, null)
            return
        }
        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(command, params, petitionService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_PETITION','ROLE_ADMIN'])
    def saveContent(CampaignContentCommand command) {
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "post.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(Long.parseLong(params.campaignId), null, command, petitionService)
            return
        }
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, petitionService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    private def petitionModelSettings(CampaignSettingsCommand command, PostRSDTO postRSDTO) {
        def model = modelSettings(command, postRSDTO)
        command.debatable=false
        model.options =[debatable:false, endDate:false]
        return model
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def signPetition(SignPetitionCommand command){
        if (command.hasErrors()){
            String msg ="No correct data"
            if (request.xhr){
                render msg
            }else{
                flash.message = msg
                redirect mapping: 'home'
            }
            return;
        }
        KuorumUser currentUser= springSecurityService.currentUser;
        PetitionRSDTO petitionRSDTO= petitionService.signPetition(command.campaignId, currentUser, command.sign, command.petitionUserId);
        render petitionRSDTO as JSON;
    }
}
