package kuorum.contest

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.contest.ContestApplicationAuthorizationsCommand
import kuorum.web.commands.payment.contest.ContestApplicationScopeCommand
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationRDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationRSDTO
import org.kuorum.rest.model.communication.contest.ContestRSDTO
import org.kuorum.rest.model.communication.contest.PageContestApplicationRSDTO
import org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.campaign.CampaignCreatorService

class ContestApplicationController extends CampaignController {

    // Grails renderer -> For CSV hack
    grails.gsp.PageRenderer groovyPageRenderer

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def create() {
        Long contestId = params.campaignId ? Long.parseLong(params.campaignId) : null
        ContestRSDTO contest = getContest(contestId)
        KuorumUserSession loggedUser = springSecurityService.principal
        int contestApplicationsCount = contestService.secureCountUserApplications(params.userAlias, contestId, loggedUser.id.toString())
        if (contest.maxApplicationsPerUser > 0 && contestApplicationsCount >= contest.maxApplicationsPerUser) {
            render(view: 'maxContestApplicationPerUserAndContest', model: [contestRSDTO: contest, contestApplicationsCount: contestApplicationsCount])
        } else if (checkRequiredProfileData(contest)) {
            return contestApplicationModelEditScope(new ContestApplicationScopeCommand(), null, contest)
        } else {
            redirect mapping: 'funnelFillBasicData', params: [campaignId: contest.id]
        }
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def saveNewApplication(ContestApplicationScopeCommand command) {
        Long contestId = params.campaignId ? Long.parseLong(params.campaignId) : null
        if (!command.validate()) {
            ContestRSDTO contest = getContest(contestId)
            render view: 'create', model: contestApplicationModelEditScope(command, null, contest)
            return
        }
        KuorumUserSession loggedUser = springSecurityService.principal
        CampaignRDTO campaignRDTO = convertCommandContentToRDTO(command, loggedUser, null, contestApplicationService)
        Map<String, Object> result = saveAndSendCampaign(loggedUser, campaignRDTO, null, null, CampaignContentCommand.CAMPAIGN_SEND_TYPE_DRAFT, contestApplicationService)
        def nextStep = result.nextStep
        redirect mapping: nextStep.mapping, params: nextStep.params

    }

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def editSettingsStep() {
        KuorumUserSession user = springSecurityService.principal
        ContestApplicationRSDTO contestApplicationRSDTO = contestApplicationService.find(user, Long.parseLong((String) params.campaignId))
        return contestModelSettings(new CampaignSettingsCommand(debatable: false), contestApplicationRSDTO)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def editContentStep() {
        Long campaignId = Long.parseLong((String) params.campaignId)
        ContestApplicationRSDTO contestApplicationRSDTO = setCampaignAsDraft(campaignId, contestApplicationService)
        return campaignModelContent(campaignId, contestApplicationRSDTO, null, contestApplicationService)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def editScopeStep() {
        Long contestApplicationId = params.campaignId ? Long.parseLong(params.campaignId) : null
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ContestApplicationRSDTO contestApplicationRSDTO = contestApplicationService.find(user.id.toString(), contestApplicationId)
        ContestRSDTO contest = contestService.find(user.id.toString(), contestApplicationRSDTO.getContest().getId())
        return contestApplicationModelEditScope(null, contestApplicationRSDTO, contest)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def saveScope(ContestApplicationScopeCommand command) {
        KuorumUserSession user = springSecurityService.principal
        Long contestApplicationId = params.campaignId ? Long.parseLong(params.campaignId) : null
        ContestApplicationRSDTO contestApplication = contestApplicationService.find(user.id.toString(), contestApplicationId)
        def nextStep = updateEnvironmentByCommand(command, contestApplication);
        redirect mapping: nextStep.mapping, params: nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def editAuthorizationsStep() {
        KuorumUserSession loggedUser = springSecurityService.principal
        ContestApplicationRSDTO contestApplicationRSDTO = contestApplicationService.find(loggedUser, Long.parseLong((String) params.campaignId))
        ContestRSDTO contest = getContest(contestApplicationRSDTO.getContest().getId())
        return contestApplicationModelEditAuthorizations(null, contestApplicationRSDTO, contest)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
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

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
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

    protected CampaignRDTO convertCommandContentToRDTO(CampaignContentCommand command, KuorumUserSession user, Long campaignId, CampaignCreatorService campaignService) {
        command.campaignVisibility = CampaignVisibilityRSDTO.VISIBLE
        ContestApplicationRDTO campaignRDTO = (ContestApplicationRDTO) super.convertCommandContentToRDTO(command, user, campaignId, campaignService)
        setCampaignName(campaignRDTO, command)
        return campaignRDTO
    }

    private void setCampaignName(ContestApplicationRDTO contestApplicationRDTO, CampaignContentCommand command) {
        if (contestApplicationRDTO.name == null) {
            contestApplicationRDTO.name = command.title
        }
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def saveAuthorizations(ContestApplicationAuthorizationsCommand command) {
        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        KuorumUserSession loggedUser = springSecurityService.principal
        if (command.hasErrors()) {
            if (command.errors.getFieldError().arguments.first() == "publishOn") {
                flash.error = message(code: "debate.scheduleError")
            }
            ContestApplicationRSDTO contestApplicationRSDTO = contestApplicationService.find(loggedUser, Long.parseLong((String) params.campaignId))
            ContestRSDTO contest = getContest(contestApplicationRSDTO.getContest().getId())
            render view: 'contestApplicationEditAuthorizations', model: contestApplicationModelEditAuthorizations(command, contestApplicationRSDTO, contest)
            return
        }
        ContestApplicationRDTO contestApplicationRSDTO = createRDTO(loggedUser, campaignId, contestApplicationService)
        contestApplicationRSDTO.authorizedAgent = command.authorizedAgent
        contestApplicationRSDTO.acceptedLegalBases = command.acceptedLegalBases
        contestApplicationRSDTO.imageRights = command.imageRights
        def result = saveAndSendCampaign(loggedUser, contestApplicationRSDTO, campaignId, command.publishOn, command.sendType, contestApplicationService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }


    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, contestApplicationService)
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


    private def contestApplicationModelEditScope(ContestApplicationScopeCommand command, ContestApplicationRSDTO contestApplicationRSDTO, ContestRSDTO contestRSDTO) {
        if (!command && contestApplicationRSDTO) {
            command = new ContestApplicationScopeCommand()
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
        return model
    }

    private def contestApplicationModelEditAuthorizations(ContestApplicationAuthorizationsCommand command, ContestApplicationRSDTO contestApplicationRSDTO, ContestRSDTO contestRSDTO) {
        if (!command && contestApplicationRSDTO) {
            command = new ContestApplicationAuthorizationsCommand()
            command.authorizedAgent = contestApplicationRSDTO.authorizedAgent
            command.acceptedLegalBases = contestApplicationRSDTO.acceptedLegalBases
            command.imageRights = contestApplicationRSDTO.imageRights
        }
        def model = [
                campaign: contestApplicationRSDTO,
                contest : contestRSDTO,
                status  : contestApplicationRSDTO?.campaignStatusRSDTO ?: CampaignStatusRSDTO.DRAFT,
                command : command
        ]
        return model
    }

    protected CampaignRDTO convertCommandContentToRDTO(ContestApplicationScopeCommand command, KuorumUserSession user, Long campaignId, CampaignCreatorService campaignService) {
        ContestApplicationRDTO campaignRDTO = (ContestApplicationRDTO) createRDTO(user, campaignId, campaignService)
        campaignRDTO.setCampaignVisibility(CampaignVisibilityRSDTO.VISIBLE)
        campaignRDTO.setTitle(command.name)
        campaignRDTO.setName(command.name)
        setContest(campaignRDTO)
        campaignRDTO.causes = [command.cause]
        campaignRDTO.focusType = command.focusType
        campaignRDTO.activityType = command.activityType
        return campaignRDTO
    }

    private void setContest(ContestApplicationRDTO contestApplicationRDTO) {
        if (!contestApplicationRDTO.contestId) {
            Long contestId = params.campaignId ? Long.parseLong(params.campaignId) : null
            contestApplicationRDTO.contestId = contestId
        }
    }

    private def updateEnvironmentByCommand(ContestApplicationScopeCommand command, ContestApplicationRSDTO contestApplicationRSDTO) {
        KuorumUserSession user = springSecurityService.principal
        Long contestId = params.contestId ? Long.parseLong(params.contestId) : null
        BasicDataKuorumUserRSDTO contestApplicationUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        ContestRSDTO contestRSDTO = contestService.find(contestApplicationUser.id.toString(), contestId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: 'editEnvironmentStep', model: contestApplicationModelEditScope(command, contestApplicationRSDTO, contestRSDTO)
            return
        }
        ContestApplicationRDTO rdto = contestApplicationService.map(contestApplicationRSDTO)
        rdto.causes = [command.cause]
        rdto.focusType = command.focusType
        rdto.activityType = command.activityType
        contestApplicationRSDTO = contestApplicationService.save(user, rdto, contestApplicationRSDTO.getId())
        def nextStep = processNextStep(user, contestApplicationRSDTO, false)
        return nextStep
    }

    private ContestRSDTO getContest(long contestId) {
        contestService.find(params.userAlias, contestId)
    }
}
