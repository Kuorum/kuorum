package kuorum.participatoryBudget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.participatoryBudget.DistrictCommand
import kuorum.web.commands.payment.participatoryBudget.DistrictsCommand
import org.kuorum.rest.model.communication.participatoryBudget.DistrictRDTO
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRDTO
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO

class ParticipatoryBudgetController extends CampaignController{

    @Secured(['ROLE_ADMIN'])
    def create() {
        return participatoryBudgetModelSettings(new CampaignSettingsCommand(debatable:true), null)
    }

    @Secured(['ROLE_CAMPAIGN_DEBATE','ROLE_CAMPAIGN_EVENT','ROLE_ADMIN'])
    def editSettingsStep(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find( user, Long.parseLong((String) params.campaignId))

        return participatoryBudgetModelSettings(new CampaignSettingsCommand(debatable:true), participatoryBudgetRSDTO)

    }

    @Secured(['ROLE_ADMIN'])
    def editContentStep(){
        Long campaignId = Long.parseLong((String) params.campaignId);
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = setCampaignAsDraft(campaignId, participatoryBudgetService)
        return campaignModelContent(campaignId, participatoryBudgetRSDTO, null, participatoryBudgetService)
    }

    @Secured(['ROLE_ADMIN'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: participatoryBudgetModelSettings(command, null)
            return
        }

        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(command, params, participatoryBudgetService)

        //flash.message = resultDebate.msg.toString()
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_ADMIN'])
    def saveContent(CampaignContentCommand command) {
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(campaignId, null,command, participatoryBudgetService)
            return
        }

        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, participatoryBudgetService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_ADMIN'])
    def editDistricts(){
        Long campaignId = Long.parseLong(params.campaignId)
        KuorumUser campaignUser = KuorumUser.get(springSecurityService.principal.id)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = setCampaignAsDraft(campaignId, participatoryBudgetService)
        if (!participatoryBudgetRSDTO.body || !participatoryBudgetRSDTO.title){
            flash.message=g.message(code:'participatoryBudget.form.nobody.redirect')
            redirect mapping: 'participatoryBudgetEditContent', params: participatoryBudgetRSDTO.encodeAsLinkProperties()
        }else{
            Long numberRecipients = getCampaignNumberRecipients(campaignUser, participatoryBudgetRSDTO)
            return [
                    campaign:participatoryBudgetRSDTO,
                    command: modelDistrictsStep(participatoryBudgetRSDTO),
                    numberRecipients:numberRecipients]
        }
    }

    private def modelDistrictsStep(ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {
        def districts
        if (participatoryBudgetRSDTO.districts){
            districts = participatoryBudgetRSDTO.districts.collect {d -> new DistrictCommand(allCity: d.allCity, name: d.name, budget: d.budget, districtId: d.id )}
        }else{
            districts = [new DistrictCommand()]
        }

        new DistrictsCommand(
                districts: districts,
                deadLineProposals: participatoryBudgetRSDTO.deadLineProposals,
                deadLineTechnicalReview: participatoryBudgetRSDTO.deadLineTechnicalReview,
                deadLineVotes: participatoryBudgetRSDTO.deadLineVotes
        )
    }

    def saveDistricts(DistrictsCommand command){
        KuorumUser campaignUser = KuorumUser.get(springSecurityService.principal.id)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(campaignUser, command.campaignId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: 'editDistricts', model: [
                    campaign:participatoryBudgetRSDTO,
                    command: command,
                    numberRecipients:getCampaignNumberRecipients(campaignUser, participatoryBudgetRSDTO)]
            return
        }
        ParticipatoryBudgetRDTO rdto = participatoryBudgetService.map(participatoryBudgetRSDTO)
        rdto.districts = command.districts?.findAll{it && it.name && it.budget}.collect {mapDistrict(it)}?:[]
        rdto.deadLineProposals = command.deadLineProposals
        rdto.deadLineTechnicalReview = command.deadLineTechnicalReview
        rdto.deadLineVotes = command.deadLineVotes
        def result = saveAndSendCampaign(campaignUser, rdto, participatoryBudgetRSDTO.getId(), command.publishOn,command.sendType, participatoryBudgetService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    private DistrictRDTO mapDistrict(DistrictCommand districtCommand) {
        new DistrictRDTO(
                id: districtCommand.districtId,
                name: districtCommand.name,
                budget: districtCommand.budget,
                allCity: districtCommand.allCity?:false
        )
    }

    @Secured(['ROLE_ADMIN'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, participatoryBudgetService);
        render ([msg: "Debate deleted"] as JSON)
    }

    private def participatoryBudgetModelSettings(CampaignSettingsCommand command, ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {
        def model = modelSettings(command, participatoryBudgetRSDTO)
        command.debatable=false
        model.options =[debatable:false, endDate:false]
        return model
    }

}
