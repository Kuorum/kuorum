package kuorum.petition

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.petition.SignPetitionCommand
import org.kuorum.rest.model.communication.petition.PetitionRSDTO
import org.kuorum.rest.model.kuorumUser.BasicUserPageRSDTO

class PetitionController extends CampaignController{

    grails.gsp.PageRenderer groovyPageRenderer

    @Secured(['ROLE_CAMPAIGN_PETITION'])
    def create() {
        return petitionModelSettings(new CampaignSettingsCommand(debatable:false), null)
    }

    @Secured(['ROLE_CAMPAIGN_PETITION'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, petitionService)
        render ([msg: "Post deleted"] as JSON)
    }

    @Secured(['ROLE_CAMPAIGN_PETITION'])
    def editSettingsStep(){
        KuorumUserSession petitionUser = springSecurityService.principal
        PetitionRSDTO petitionRSDTO = petitionService.find(petitionUser, Long.parseLong(params.campaignId))
        return petitionModelSettings(new CampaignSettingsCommand(debatable:false), petitionRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_PETITION'])
    def editContentStep(){
        Long campaignId = Long.parseLong(params.campaignId)
        PetitionRSDTO petitionRSDTO = setCampaignAsDraft(campaignId, petitionService)
        return campaignModelContent(campaignId, petitionRSDTO, null, petitionService)
    }

    @Secured(['ROLE_CAMPAIGN_PETITION'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: petitionModelSettings(command, null)
            return
        }
        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(command, params, petitionService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_PETITION'])
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

    private def petitionModelSettings(CampaignSettingsCommand command, PetitionRSDTO petitionRSDTO) {
        def model = modelSettings(command, petitionRSDTO)
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
            return
        }
        KuorumUserSession currentUser= springSecurityService.principal
        PetitionRSDTO petitionRSDTO= petitionService.signPetition(command.campaignId, currentUser, command.sign, command.petitionUserId)
        BasicUserPageRSDTO signs = petitionService.listSigns(petitionRSDTO.id, 0, 20)
        def signsHtml = groovyPageRenderer.render(template: '/petition/showModules/signedUsersContent', model: [petition: petitionRSDTO, signs: signs.getData()])
        render ([signsHtml:signsHtml, petition: petitionRSDTO] as JSON)
    }

//    def getPetitionSigns(){
//        Long campaignId = Long.parseLong(params.campaignId)
//        Integer page = params.page?Integer.parseInt(params.page):0;
//        Integer size = params.size?Integer.parseInt(params.size):10;
//        BasicUserPageRSDTO signs = petitionService.listSigns(campaignId, page, size)
//        render
//    }
}
