package kuorum.participatoryBudget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand
import kuorum.web.commands.payment.participatoryBudget.NewDistrictProposalWithDistrictCommand
import org.kuorum.rest.model.communication.CampaignLightPageRSDTO
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.CampaignTypeRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO
import org.kuorum.rest.model.communication.search.SearchCampaignRDTO
import org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.validation.UserValidationRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.campaign.CampaignCreatorService

class DistrictProposalController extends CampaignController {

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL'])
    def create() {
        Long participatoryBudgetId = params.campaignId ? Long.parseLong(params.campaignId) : null
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(user.id.toString(), participatoryBudgetId)
        if (participatoryBudgetRSDTO.addProposalsWithValidation){
            KuorumUserSession userSession = springSecurityService.principal
            UserValidationRSDTO userValidationRSDTO = kuorumUserService.getUserValidationStatus(userSession, participatoryBudgetId)
            if (!userValidationRSDTO.isGranted()){
                // USER NEEDS TO BE VALIDATED to add a proposal
                redirect mapping:'campaignValidationInitProcess', params: participatoryBudgetRSDTO.encodeAsLinkProperties()
                return ;
            }
        }
        def districtProposalsCounters = countNumDistrictProposalsPerBudget(participatoryBudgetRSDTO, participatoryBudgetId)
        if (districtProposalsCounters.numProposals >= participatoryBudgetRSDTO.maxDistrictProposalsPerUser) {
            render(view: 'maxDistrictProposalsPerUserAndBudget', model: [participatoryBudgetRSDTO: participatoryBudgetRSDTO, districtProposalsCounters: districtProposalsCounters])
        } else {
            DistrictProposalChooseDistrictCommand command = new DistrictProposalChooseDistrictCommand()
            if (participatoryBudgetRSDTO.districts && participatoryBudgetRSDTO.districts.size()==1){
                // If there is only one district, the district is preselected to the unique district.
                command.setDistrictId(participatoryBudgetRSDTO.districts.get(0).getId())
            }
            return districtProposalModelEditDistrict(command, null, participatoryBudgetRSDTO)
        }
    }


    private def countNumDistrictProposalsPerBudget(ParticipatoryBudgetRSDTO participatoryBudgetRSDTO, participatoryBudgetId) {
        KuorumUserSession user = springSecurityService.principal
        SearchCampaignRDTO searchCampaignRDTO = new SearchCampaignRDTO(
                page:0,
                size: 1,
                attachNotPublished: true,
                onlyPublications: false,
                campaignType: CampaignTypeRSDTO.DISTRICT_PROPOSAL,
                participatoryBudgetId: participatoryBudgetId
        )
        CampaignLightPageRSDTO campaigns = campaignService.findAllCampaigns( user,searchCampaignRDTO)
        return [
                numProposals     : campaigns.getTotal(),
                numProposalsDraft: campaigns.getTotal()
        ]
    }

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL'])
    def saveNewProposal(DistrictProposalChooseDistrictCommand command) {
        Long participatoryBudgetId = params.campaignId ? Long.parseLong(params.campaignId) : null
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(user.id.toString(), participatoryBudgetId)
        if (command.hasErrors()) {
            render view: 'create', model: districtProposalModelEditDistrict(command, null, participatoryBudgetRSDTO)
            return
        }

        NewDistrictProposalWithDistrictCommand contentCommand = new NewDistrictProposalWithDistrictCommand(title: command.name);
        contentCommand.title = command.name
        contentCommand.districtId = command.districtId
        contentCommand.cause = command.cause
        contentCommand.backerType = command.backerType
        contentCommand.campaignVisibility = CampaignVisibilityRSDTO.VISIBLE
        Map<String, Object> result = saveAndSendCampaignContent(contentCommand, null, districtProposalService)
        def nextStep = updateDistrictByCommand(command, result.campaign);
        redirect mapping: nextStep.mapping, params: nextStep.params
    }

    private districtProposalModelContennt(ParticipatoryBudgetRSDTO participatoryBudgetRSDTO, Long districtProposalId, DistrictProposalRSDTO districtProposalRSDTO = null, CampaignContentCommand command = null) {
        participatoryBudgetRSDTO
        def model = campaignModelContent(districtProposalId, districtProposalRSDTO, command, districtProposalService)
        model['participatoryBudget'] = participatoryBudgetRSDTO
        return model
    }

    protected CampaignRDTO convertCommandContentToRDTO(CampaignContentCommand command, KuorumUserSession user, Long campaignId, CampaignCreatorService campaignService) {
        command.campaignVisibility = CampaignVisibilityRSDTO.VISIBLE
        DistrictProposalRDTO campaignRDTO = (DistrictProposalRDTO) super.convertCommandContentToRDTO(command, user, campaignId, campaignService)
        setCampaignName(campaignRDTO, command)
        setParticipatoryBudget(campaignRDTO)
        if (command instanceof NewDistrictProposalWithDistrictCommand) {
            setDistrictProposalFields(campaignRDTO, command)
        }
        return campaignRDTO
    }

    private void setDistrictProposalFields(DistrictProposalRDTO districtProposalRDTO, NewDistrictProposalWithDistrictCommand newDistrictProposalWithDistrictCommand) {
        districtProposalRDTO.districtId = newDistrictProposalWithDistrictCommand.districtId
        districtProposalRDTO.causes = [newDistrictProposalWithDistrictCommand.cause]
        districtProposalRDTO.backerType = newDistrictProposalWithDistrictCommand.backerType
    }

    private void setParticipatoryBudget(DistrictProposalRDTO districtProposalRDTO) {
        if (!districtProposalRDTO.participatoryBudgetId) {
            Long participatoryBudgetId = params.campaignId ? Long.parseLong(params.campaignId) : null
            districtProposalRDTO.participatoryBudgetId = participatoryBudgetId
        }
    }

    /************************************************/
    /********** STANDARD EDITION CAMPAIGN ***********/
    /************************************************/

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL'])
    def editSettingsStep() {
        KuorumUserSession user = springSecurityService.principal
        DistrictProposalRSDTO districtProposalRSDTO = districtProposalService.find(user, Long.parseLong((String) params.campaignId))

        return districtProposalModelSettings(new CampaignSettingsCommand(debatable: false), districtProposalRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL'])
    def editDistrict() {
        Long participatoryBudgetId = params.participatoryBudgetId ? Long.parseLong(params.participatoryBudgetId) : null
        BasicDataKuorumUserRSDTO participatoryBudgetUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(participatoryBudgetUser.id.toString(), participatoryBudgetId)

        KuorumUserSession user = springSecurityService.principal
        DistrictProposalRSDTO districtProposalRSDTO = districtProposalService.find(user, Long.parseLong((String) params.campaignId))

        return districtProposalModelEditDistrict(null, districtProposalRSDTO, participatoryBudgetRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL'])
    def editContentStep() {
        Long participatoryBudgetId = params.participatoryBudgetId ? Long.parseLong(params.participatoryBudgetId) : null
        BasicDataKuorumUserRSDTO participatoryBudgetUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(participatoryBudgetUser.id.toString(), participatoryBudgetId)

        Long campaignId = Long.parseLong((String) params.campaignId)
        DistrictProposalRSDTO districtProposalRSDTO = setCampaignAsDraft(campaignId, districtProposalService)
        return districtProposalModelContennt(participatoryBudgetRSDTO, campaignId, districtProposalRSDTO, null)
    }

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: districtProposalModelSettings(command, null)
            return
        }

        command.eventAttached = false
        Map<String, Object> result = saveCampaignSettings(command, params, districtProposalService)

        //flash.message = resultDebate.msg.toString()
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL'])
    def saveContent(CampaignContentCommand command) {
        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        if (command.hasErrors()) {
            Long participatoryBudgetId = params.participatoryBudgetId ? Long.parseLong(params.participatoryBudgetId) : null
            BasicDataKuorumUserRSDTO participatoryBudgetUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
            ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(participatoryBudgetUser.id.toString(), participatoryBudgetId)


            if (command.errors.getFieldError().arguments.first() == "publishOn") {
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContentStep', model: districtProposalModelContennt(participatoryBudgetRSDTO, campaignId, null, command)
            return
        }

        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, districtProposalService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL'])
    def saveDistrict(DistrictProposalChooseDistrictCommand command) {
        KuorumUserSession user = springSecurityService.principal
        DistrictProposalRSDTO districtProposalRSDTO = districtProposalService.find(user, Long.parseLong((String) params.campaignId))

        def nextStep = updateDistrictByCommand(command, districtProposalRSDTO);
        redirect mapping: nextStep.mapping, params: nextStep.params
    }

    private def updateDistrictByCommand(DistrictProposalChooseDistrictCommand command, DistrictProposalRSDTO districtProposalRSDTO) {
        KuorumUserSession user = springSecurityService.principal
        Long participatoryBudgetId = params.participatoryBudgetId ? Long.parseLong(params.participatoryBudgetId) : null
        BasicDataKuorumUserRSDTO participatoryBudgetUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(participatoryBudgetUser.id.toString(), participatoryBudgetId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: 'editDistrict', model: districtProposalModelEditDistrict(command, districtProposalRSDTO, participatoryBudgetRSDTO)
            return
        }
        DistrictProposalRDTO rdto = districtProposalService.map(districtProposalRSDTO)
        rdto.districtId = command.districtId
        rdto.causes = [command.cause]
        rdto.backerType = command.backerType
        districtProposalRSDTO = districtProposalService.save(user, rdto, districtProposalRSDTO.getId())
        def nextStep = processNextStep(user, districtProposalRSDTO, false)
        return nextStep;
    }

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, districtProposalService)
        render([msg: "District proposal deleted"] as JSON)
    }

    private def districtProposalModelSettings(CampaignSettingsCommand command, DistrictProposalRSDTO districtProposalRSDTO) {
        def model = modelSettings(command, districtProposalRSDTO)
        command.debatable = false
        model.options = [debatable: false, hideCauses: true]
        return model
    }

    private def districtProposalModelEditDistrict(DistrictProposalChooseDistrictCommand command, DistrictProposalRSDTO districtProposalRSDTO, ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {
        if (!command && districtProposalRSDTO) {
            command = new DistrictProposalChooseDistrictCommand()
            command.districtId = districtProposalRSDTO.district.id
            command.cause = districtProposalRSDTO.causes ? districtProposalRSDTO.causes[0] : ""
            command.backerType = districtProposalRSDTO.backerType
        }
        def model = [
                campaign           : districtProposalRSDTO,
                participatoryBudget: participatoryBudgetRSDTO,
                status             : districtProposalRSDTO?.campaignStatusRSDTO ?: CampaignStatusRSDTO.DRAFT,
                command            : command
        ]
        model.options = [debatable: false]
        return model
    }


}
