package kuorum.participatoryBudget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.campaign.CampaignCreatorService

class DistrictProposalController extends CampaignController{

    private static final SESSION_KEY_DISTRICT_COMMAND = "SESSION_KEY_DISTRICT_COMMAND";


    /********************************************************************/
    /********** CREATE CAMPAIGN STORING TEMPORALLY ON SESSION ***********/
    /********************************************************************/

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def create() {
        Long participatoryBudgetId = params.campaignId?Long.parseLong(params.campaignId):null
        KuorumUser kuorumUser = kuorumUserService.findByAlias(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(kuorumUser, participatoryBudgetId)
        return districtProposalModelEditDistrict(new DistrictProposalChooseDistrictCommand(), null,participatoryBudgetRSDTO)
    }


    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def saveNewProposal(DistrictProposalChooseDistrictCommand command){
        Long participatoryBudgetId = params.campaignId?Long.parseLong(params.campaignId):null
        KuorumUser kuorumUser = kuorumUserService.findByAlias(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(kuorumUser, participatoryBudgetId)
        if (command.hasErrors()){
            render view: 'create', model: districtProposalModelEditDistrict(command, null, participatoryBudgetRSDTO)
            return
        }

        // SAVE DISTRICT AN CAUSE IN SESSION. IT WILL BE USED ON CONTENT STEP WHERE WE KNOW THE NAME OF THE CAMPAIGN
        request.getSession().setAttribute(SESSION_KEY_DISTRICT_COMMAND, command)

        redirect mapping: params.redirectLink, params: participatoryBudgetRSDTO.encodeAsLinkProperties()

    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def createByContent(){

        Long participatoryBudgetId = params.campaignId?Long.parseLong(params.campaignId):null
        KuorumUser kuorumUser = kuorumUserService.findByAlias(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(kuorumUser, participatoryBudgetId)
        DistrictProposalChooseDistrictCommand districtCommand = (DistrictProposalChooseDistrictCommand)request.getSession().getAttribute(SESSION_KEY_DISTRICT_COMMAND)
        if (!districtCommand){
            flash.message = "Please choose a district"
            redirect mapping:'districtProposalCreate', params: participatoryBudgetRSDTO.encodeAsLinkProperties()
        }else{
            return campaignModelContent(null, null, null, districtProposalService)
        }
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def saveNewProposalByContent(CampaignContentCommand command){
        Long participatoryBudgetId = params.campaignId?Long.parseLong(params.campaignId):null
        KuorumUser kuorumUser = kuorumUserService.findByAlias(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(kuorumUser, participatoryBudgetId)
        DistrictProposalChooseDistrictCommand districtCommand = (DistrictProposalChooseDistrictCommand)request.getSession().getAttribute(SESSION_KEY_DISTRICT_COMMAND)
        if (!districtCommand){
            flash.message = "Please choose a district"
            redirect mapping:'districtProposalCreate', params: participatoryBudgetRSDTO.encodeAsLinkProperties()
        }
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'createByContent', model: campaignModelContent(null, null,command, districtProposalService)
            return
        }

        Map<String, Object> result = saveAndSendCampaignContent(command, null, districtProposalService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    protected CampaignRDTO convertCommandContentToRDTO(CampaignContentCommand command, KuorumUser user, Long campaignId, CampaignCreatorService campaignService) {
        DistrictProposalRDTO campaignRDTO = (DistrictProposalRDTO)super.convertCommandContentToRDTO(command, user, campaignId, campaignService)
        setDistrictFromSession(campaignRDTO)
        setCampaignName(campaignRDTO, command)
        setParticipatoryBudget(campaignRDTO)
        return campaignRDTO
    }

    private void setDistrictFromSession(DistrictProposalRDTO districtProposalRDTO){
        DistrictProposalChooseDistrictCommand districtCommand = (DistrictProposalChooseDistrictCommand)request.getSession().getAttribute(SESSION_KEY_DISTRICT_COMMAND)
        if (!districtProposalRDTO.districtId && districtCommand){
            districtProposalRDTO.setDistrictId(districtCommand.districtId)
            districtProposalRDTO.setCauses([districtCommand.cause])
        }
        request.getSession().removeAttribute(SESSION_KEY_DISTRICT_COMMAND)
    }

    private void setCampaignName(DistrictProposalRDTO districtProposalRDTO, CampaignContentCommand command){
        if (districtProposalRDTO.name == null){
            districtProposalRDTO.name = command.title
        }
    }

    private void setParticipatoryBudget(DistrictProposalRDTO districtProposalRDTO){
        if (!districtProposalRDTO.participatoryBudgetId){
            Long participatoryBudgetId = params.campaignId?Long.parseLong(params.campaignId):null
            districtProposalRDTO.participatoryBudgetId = participatoryBudgetId
        }
    }

    /************************************************/
    /********** STANDARD EDITION CAMPAIGN ***********/
    /************************************************/

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def editSettingsStep(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DistrictProposalRSDTO districtProposalRSDTO = districtProposalService.find( user, Long.parseLong((String) params.campaignId))

        return districtProposalModelSettings(new CampaignSettingsCommand(debatable:true), districtProposalRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def editDistrict(){
        Long participatoryBudgetId = params.participatoryBudgetId?Long.parseLong(params.participatoryBudgetId):null
        KuorumUser kuorumUser = kuorumUserService.findByAlias(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(kuorumUser, participatoryBudgetId)

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DistrictProposalRSDTO districtProposalRSDTO = districtProposalService.find( user, Long.parseLong((String) params.campaignId))

        return districtProposalModelEditDistrict(null, districtProposalRSDTO, participatoryBudgetRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def editContentStep(){
        Long campaignId = Long.parseLong((String) params.campaignId);
        DistrictProposalRSDTO districtProposalRSDTO = setCampaignAsDraft(campaignId, districtProposalService)
        return campaignModelContent(campaignId, districtProposalRSDTO, null, districtProposalService)
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: districtProposalModelSettings(command, null)
            return
        }

        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(command, params, districtProposalService)

        //flash.message = resultDebate.msg.toString()
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def saveContent(CampaignContentCommand command) {
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(campaignId, null,command, districtProposalService)
            return
        }

        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, districtProposalService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def saveDistrict(DistrictProposalChooseDistrictCommand command){
        Long participatoryBudgetId = params.participatoryBudgetId?Long.parseLong(params.participatoryBudgetId):null
        KuorumUser kuorumUser = kuorumUserService.findByAlias(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(kuorumUser, participatoryBudgetId)

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        DistrictProposalRSDTO districtProposalRSDTO = districtProposalService.find( user, Long.parseLong((String) params.campaignId))

        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: 'editDistrict', model: districtProposalModelEditDistrict(command, districtProposalRSDTO, participatoryBudgetRSDTO)
            return
        }
        DistrictProposalRDTO rdto = districtProposalService.map(districtProposalRSDTO)
        rdto.districtId = command.districtId
        districtProposalRSDTO = districtProposalService.save(user, rdto, districtProposalRSDTO.getId())
        def nextStep = processNextStep(user, districtProposalRSDTO, false)
        redirect mapping: nextStep.mapping, params: nextStep.params

    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, districtProposalService);
        render ([msg: "Debate deleted"] as JSON)
    }

    private def districtProposalModelSettings(CampaignSettingsCommand command, DistrictProposalRSDTO districtProposalRSDTO) {
        def model = modelSettings(command, districtProposalRSDTO)
        command.debatable=false
        model.options =[debatable:false, endDate:false]
        return model
    }
    private def districtProposalModelEditDistrict(DistrictProposalChooseDistrictCommand command, DistrictProposalRSDTO districtProposalRSDTO, ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {
        if (!command && districtProposalRSDTO){
            command = new DistrictProposalChooseDistrictCommand()
            command.districtId = districtProposalRSDTO.district.id
            command.cause = districtProposalRSDTO.causes?districtProposalRSDTO.causes[0]:""
        }
        def model = [
                campaign:districtProposalRSDTO,
                participatoryBudget: participatoryBudgetRSDTO,
                status:districtProposalRSDTO?.campaignStatusRSDTO?:CampaignStatusRSDTO.DRAFT,
                command:command
        ]
        model.options =[debatable:false, endDate:false]
        return model
    }
}