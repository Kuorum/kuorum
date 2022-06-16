package kuorum.contest

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.contest.ContestDeadlinesCommand
import kuorum.web.commands.payment.participatoryBudget.*
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.CampaignLightRSDTO
import org.kuorum.rest.model.communication.contest.ContestRDTO
import org.kuorum.rest.model.communication.contest.ContestRSDTO
import org.kuorum.rest.model.communication.contest.ContestStatusDTO
import org.kuorum.rest.model.communication.contest.FilterContestApplicationRDTO
import org.kuorum.rest.model.communication.contest.PageContestApplicationRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.*
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

import java.lang.reflect.UndeclaredThrowableException

class ContestController extends CampaignController {

    // Grails renderer -> For CSV hack
    grails.gsp.PageRenderer groovyPageRenderer

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def create() {
        return contestModelSettings(new CampaignSettingsCommand(debatable: false), null)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def editSettingsStep() {
        KuorumUserSession user = springSecurityService.principal
        ContestRSDTO contestRSDTO = contestService.find(user, Long.parseLong((String) params.campaignId))
        return contestModelSettings(new CampaignSettingsCommand(debatable: false), contestRSDTO)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def editContentStep() {
        Long campaignId = Long.parseLong((String) params.campaignId)
        ContestRSDTO contestRSDTO = setCampaignAsDraft(campaignId, contestService)
        return campaignModelContent(campaignId, contestRSDTO, null, contestService)
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: contestModelSettings(command, null)
            return
        }

        command.eventAttached = false
        Map<String, Object> result = saveCampaignSettings(command, params, contestService)

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
            render view: 'editContentStep', model: campaignModelContent(campaignId, null, command, contestService)
            return
        }

        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, contestService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def editDeadlines() {
        Long campaignId = Long.parseLong(params.campaignId)
        ContestRSDTO contestRSDTO = setCampaignAsDraft(campaignId, contestService)
        if (!contestRSDTO.body || !contestRSDTO.title) {
            flash.message = g.message(code: 'contest.form.nobody.redirect')
            redirect mapping: 'contestEditContent', params: contestRSDTO.encodeAsLinkProperties()
        } else {
            return [
                    campaign: contestRSDTO,
                    command : buildCommandDeadlinesStep(contestRSDTO)
            ]
        }
    }

    private ContestDeadlinesCommand buildCommandDeadlinesStep(ContestRSDTO contestRSDTO) {

        new ContestDeadlinesCommand(
                campaignId: contestRSDTO.id,
                deadLineApplications: contestRSDTO.deadLineApplications,
                deadLineReview: contestRSDTO.deadLineReview,
                deadLineVotes: contestRSDTO.deadLineVotes,
                deadLineResults: contestRSDTO.deadLineResults,
                numWinnerApplications: contestRSDTO.numWinnerApplications
        )
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def saveDeadlines(ContestDeadlinesCommand command) {
        KuorumUserSession campaignUser = springSecurityService.principal
        ContestRSDTO contestRSDTO = contestService.find(campaignUser, command.campaignId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: '/contest/editDeadlines', model: [
                    campaign: contestRSDTO,
                    command : command]
            return
        }
        ContestRDTO rdto = contestService.map(contestRSDTO)
        rdto.deadLineApplications = command.deadLineApplications
        rdto.deadLineReview = command.deadLineReview
        rdto.deadLineVotes = command.deadLineVotes
        rdto.deadLineResults = command.deadLineResults
        rdto.numWinnerApplications = command.numWinnerApplications
        def result = saveAndSendCampaign(campaignUser, rdto, contestRSDTO.getId(), null, null, contestService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, participatoryBudgetService)
        render([msg: "Contest budget deleted"] as JSON)
    }

    private def contestModelSettings(CampaignSettingsCommand command, ContestRSDTO contestRSDTO) {
        def model = modelSettings(command, contestRSDTO)
        command.debatable = false
        model.options = [debatable: false]
        return model
    }

    /******************/
    /***** END CRUD ***/
    /******************/


    def copy(Long campaignId) {
        return super.copyCampaign(campaignId, contestService)
    }


    def findContestApplications() {
        BasicDataKuorumUserRSDTO contestUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        Long participatoryBudgetId = Long.parseLong(params.campaignId)
        Integer page = params.page ? Integer.parseInt(params.page) : 0
        String viewerUid = cookieUUIDService.buildUserUUID()
        FilterContestApplicationRDTO filter = new FilterContestApplicationRDTO(page: page)
        if (params.randomSeed) {
            Double randomSeed = Double.parseDouble(params.randomSeed)
            filter.sort = new FilterContestApplicationRDTO.SortContestApplicationRDTO(randomSeed: randomSeed)
        } else {
            filter.sort = new FilterContestApplicationRDTO.SortContestApplicationRDTO(field: FilterContestApplicationRDTO.SortableFieldRDTO.VOTES, direction: FilterContestApplicationRDTO.DirectionRDTO.ASC)
        }

        if (filter.sort && params.direction) {
            FilterContestApplicationRDTO.DirectionRDTO dir = FilterContestApplicationRDTO.DirectionRDTO.valueOf(params.direction)
            filter.sort.direction = dir
        }
        PageContestApplicationRSDTO pageContestApplications = contestService.findContestApplications(contestUser, participatoryBudgetId, filter, viewerUid)
        if (pageContestApplications.total == 0) {
            render template: '/contest/showModules/mainContent/contestApplicationsEmpty'
        } else {
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${pageContestApplications.total > ((pageContestApplications.page + 1) * pageContestApplications.size)}")
            render template: '/campaigns/cards/campaignsList', model: [campaigns: pageContestApplications.data, showAuthor: true]
        }
    }


}
