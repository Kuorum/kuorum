package kuorum.contest

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.contest.ContestApplicationEnvironmentCommand
import kuorum.web.commands.payment.contest.ContestDeadlinesCommand
import kuorum.web.commands.payment.contest.NewContestApplicationCommand
import kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand
import kuorum.web.commands.payment.participatoryBudget.NewDistrictProposalWithDistrictCommand
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationRDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationRSDTO
import org.kuorum.rest.model.communication.contest.ContestRDTO
import org.kuorum.rest.model.communication.contest.ContestRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO
import org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.campaign.CampaignCreatorService

class ContestApplicationController extends CampaignController {

    // Grails renderer -> For CSV hack
    grails.gsp.PageRenderer groovyPageRenderer

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def create() {
        Long contestId = params.campaignId ? Long.parseLong(params.campaignId) : null
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ContestRSDTO contest = contestService.find(user.id.toString(), contestId)
        return contestApplicationModelEditEnvironment(new ContestApplicationEnvironmentCommand(), null, contest)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def saveNewApplication(ContestApplicationEnvironmentCommand command) {
        Long contestId = params.campaignId ? Long.parseLong(params.campaignId) : null
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ContestRSDTO contest = contestService.find(user.id.toString(), contestId)
        if (!command.validate()) {
            render view: 'create', model: contestApplicationModelEditEnvironment(command, null, contest)
            return
        }

        NewContestApplicationCommand contentCommand = new NewContestApplicationCommand(title: command.name);
        contentCommand.title = command.name
        contentCommand.cause = command.cause
        contentCommand.activityType = command.activityType
        contentCommand.focusType = command.focusType
        contentCommand.campaignVisibility = CampaignVisibilityRSDTO.VISIBLE
        contentCommand.contestId = contestId
        Map<String, Object> result = saveAndSendCampaignContent(contentCommand, null, contestApplicationService)
        def nextStep = result.nextStep
        redirect mapping: nextStep.mapping, params: nextStep.params

    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def editSettingsStep() {
        KuorumUserSession user = springSecurityService.principal
        ContestApplicationRSDTO contestApplicationRSDTO = contestApplicationService.find(user, Long.parseLong((String) params.campaignId))
        return contestModelSettings(new CampaignSettingsCommand(debatable: false), contestApplicationRSDTO)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def editContentStep() {
        Long campaignId = Long.parseLong((String) params.campaignId)
        ContestApplicationRSDTO contestApplicationRSDTO = setCampaignAsDraft(campaignId, contestApplicationService)
        return campaignModelContent(campaignId, contestApplicationRSDTO, null, contestApplicationService)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def editEnvironmentStep() {
        Long contestApplicationId = params.campaignId ? Long.parseLong(params.campaignId) : null
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ContestApplicationRSDTO contestApplicationRSDTO = contestApplicationService.find(user.id.toString(), contestApplicationId)
        ContestRSDTO contest = contestService.find(user.id.toString(), contestApplicationRSDTO.getContest().getId())
        return contestApplicationModelEditEnvironment(null, contestApplicationRSDTO, contest)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def saveEnvironment(ContestApplicationEnvironmentCommand command) {
        KuorumUserSession user = springSecurityService.principal
        Long contestApplicationId = params.campaignId ? Long.parseLong(params.campaignId) : null
        ContestApplicationRSDTO contestApplication = contestApplicationService.find(user.id.toString(), contestApplicationId)
        def nextStep = updateEnvironmentByCommand(command, contestApplication);
        redirect mapping: nextStep.mapping, params: nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: contestModelSettings(command, null)
            return
        }
        command.eventAttached = false
        Map<String, Object> result = saveCampaignSettings(command, params, contestApplicationService)
        //flash.message = resultDebate.msg.toString()
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def saveContent(CampaignContentCommand command) {
        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        if (command.hasErrors()) {
            if (command.errors.getFieldError().arguments.first() == "publishOn") {
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(campaignId, null, command, contestApplicationService)
            return
        }

        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, contestApplicationService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }


    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, participatoryBudgetService)
        render([msg: "Contest budget deleted"] as JSON)
    }

    private def contestModelSettings(CampaignSettingsCommand command, ContestApplicationRSDTO contestApplicationRSDTO) {
        def model = modelSettings(command, contestApplicationRSDTO)
        command.debatable = false
        model.options = [debatable: false]
        return model
    }

    def copy(Long campaignId) {
        return super.copyCampaign(campaignId, contestApplicationService)
    }

    /******************/
    /***** END CRUD ***/
    /******************/


    private def contestApplicationModelEditEnvironment(ContestApplicationEnvironmentCommand command, ContestApplicationRSDTO contestApplicationRSDTO, ContestRSDTO contestRSDTO) {
        if (!command && contestApplicationRSDTO) {
            command = new ContestApplicationEnvironmentCommand()
            command.cause = contestApplicationRSDTO.causes ? contestApplicationRSDTO.causes[0] : ""
            command.focusType = contestApplicationRSDTO.focusType
            command.activityType = contestApplicationRSDTO.activityType
        }
        def model = [
                campaign: contestApplicationRSDTO,
                contest : contestRSDTO,
                status  : contestApplicationRSDTO?.campaignStatusRSDTO ?: CampaignStatusRSDTO.DRAFT,
                command : command
        ]
        model.options = [debatable: false]
        return model
    }

    protected CampaignRDTO convertCommandContentToRDTO(CampaignContentCommand command, KuorumUserSession user, Long campaignId, CampaignCreatorService campaignService) {
        command.campaignVisibility = CampaignVisibilityRSDTO.VISIBLE
        ContestApplicationRDTO campaignRDTO = (ContestApplicationRDTO) super.convertCommandContentToRDTO(command, user, campaignId, campaignService)
        setCampaignName(campaignRDTO, command)
        setContest(campaignRDTO)
        if (command instanceof NewContestApplicationCommand) {
            campaignRDTO.causes = [command.cause]
            campaignRDTO.focusType = command.focusType
            campaignRDTO.activityType = command.activityType
        }
        return campaignRDTO
    }

    private void setContest(ContestApplicationRDTO contestApplicationRDTO) {
        if (!contestApplicationRDTO.contestId) {
            Long contestId = params.campaignId ? Long.parseLong(params.campaignId) : null
            contestApplicationRDTO.contestId = contestId
        }
    }

    private def updateEnvironmentByCommand(ContestApplicationEnvironmentCommand command, ContestApplicationRSDTO contestApplicationRSDTO) {
        KuorumUserSession user = springSecurityService.principal
        Long contestId = params.contestId ? Long.parseLong(params.contestId) : null
        BasicDataKuorumUserRSDTO contestApplicationUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ContestRSDTO contestRSDTO = contestService.find(contestApplicationUser.id.toString(), contestId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: 'editEnvironmentStep', model: contestApplicationModelEditEnvironment(command, contestApplicationRSDTO, contestRSDTO)
            return
        }
        ContestApplicationRDTO rdto = contestApplicationService.map(contestApplicationRSDTO)
        rdto.causes = [command.cause]
        rdto.focusType = command.focusType
        rdto.activityType = command.activityType
        contestApplicationRSDTO = contestApplicationService.save(user, rdto, contestApplicationRSDTO.getId())
        def nextStep = processNextStep(user, contestApplicationRSDTO, false)
        return nextStep;
    }
}
