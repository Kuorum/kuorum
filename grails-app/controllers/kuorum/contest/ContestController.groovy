package kuorum.contest

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.util.TimeZoneUtil
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.contest.*

import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.contest.*
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.search.DirectionDTO

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
    def editStatus(ContestChangeStatusCommand command) {

        KuorumUserSession loggedUser = springSecurityService.principal
        ContestRSDTO contestRSDTO = contestService.find(loggedUser, command.campaignId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            redirect mapping: 'contestShow', params: contestRSDTO.encodeAsLinkProperties()
            return
        }
        ContestRDTO rdto = contestService.map(contestRSDTO)
        rdto.setStatus(command.getStatus())
        String msgError = null
        try {
            contestService.save(loggedUser, rdto, command.campaignId)
        } catch (UndeclaredThrowableException e) {
            if (e.undeclaredThrowable.cause instanceof KuorumException) {
                KuorumException ke = e.undeclaredThrowable.cause
                msgError = message(code: ke.errors[0].code)
            } else {
                msgError = "Error updating contest status"
            }
        }
        if (request.xhr) {
            render([success: (msgError == null), msg: msgError] as JSON)
        } else {
            if (msgError) {
                flash.error = msgError
            }
            redirect mapping: 'contestShow', params: contestRSDTO.encodeAsLinkProperties()
        }
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
                deadLineResults: contestRSDTO.deadLineResults
        )
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def saveDeadlines(ContestDeadlinesCommand command) {
        KuorumUserSession campaignUser = springSecurityService.principal
        ContestRSDTO contestRSDTO = contestService.find(params.userAlias, command.campaignId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: '/contest/editDeadlines', model: [
                    campaign: contestRSDTO,
                    command : command]
            return
        }
        ContestRDTO rdto = contestService.map(contestRSDTO)
        rdto.deadLineApplications = TimeZoneUtil.convertToUserTimeZone(command.deadLineApplications, campaignUser.timeZone)
        rdto.deadLineReview = TimeZoneUtil.convertToUserTimeZone(command.deadLineReview, campaignUser.timeZone)
        rdto.deadLineVotes = TimeZoneUtil.convertToUserTimeZone(command.deadLineVotes, campaignUser.timeZone)
        rdto.deadLineResults = TimeZoneUtil.convertToUserTimeZone(command.deadLineResults, campaignUser.timeZone)
        def result = saveAndSendCampaign(campaignUser, rdto, contestRSDTO.getId(), null, null, contestService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def editContestConfig() {
        Long campaignId = Long.parseLong(params.campaignId)
        ContestRSDTO contestRSDTO = setCampaignAsDraft(campaignId, contestService)
        if (!contestRSDTO.body || !contestRSDTO.title) {
            flash.message = g.message(code: 'contest.form.nobody.redirect')
            redirect mapping: 'contestEditContent', params: contestRSDTO.encodeAsLinkProperties()
        } else if (!contestRSDTO.deadLineResults || !contestRSDTO.deadLineReview || !contestRSDTO.deadLineApplications || !contestRSDTO.deadLineVotes) {
            flash.message = g.message(code: 'contest.form.nobody.redirect')
            redirect mapping: 'contestEditDeadlines', params: contestRSDTO.encodeAsLinkProperties()
        } else {
            return campaignModelContestArea(campaignId, contestRSDTO, new ContestAreasCommand())
        }
    }


    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def saveContestConfig(ContestAreasCommand command) {
        KuorumUserSession campaignUser = springSecurityService.principal
        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        ContestRSDTO contestRSDTO = contestService.find(campaignUser, command.campaignId)
        if (command.hasErrors()) {
            if (command.errors.getFieldError().arguments.first() == "publishOn") {
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContestAreas', model: campaignModelContestArea(campaignId, contestRSDTO, command)
            return
        }
        ContestRDTO rdto = contestService.map(contestRSDTO)
//        rdto.causes = command.causes
        rdto.numWinnerApplications = command.numWinnerApplications
        rdto.maxApplicationsPerUser = command.maxApplicationsPerUser
        def result = saveAndSendCampaign(campaignUser, rdto, contestRSDTO.getId(), command.publishOn, command.sendType, contestService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }


    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, contestService)
        render([msg: "Contest budget deleted"] as JSON)
    }

    private def contestModelSettings(CampaignSettingsCommand command, ContestRSDTO contestRSDTO) {
        def model = modelSettings(command, contestRSDTO)
        command.debatable = false
        if (contestRSDTO == null) {
            // By default the creation of a contest, profile controler is active
            command.profileComplete = true
        }
        model.options = [debatable: false, configProfileComplete: true]
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
        Long contestId = Long.parseLong(params.campaignId)
        Integer page = params.page ? Integer.parseInt(params.page) : 0
        String viewerUid = cookieUUIDService.buildUserUUID()
        FilterContestApplicationRDTO filter = new FilterContestApplicationRDTO(page: page)
        if (params.randomSeed) {
            Double randomSeed = Double.parseDouble(params.randomSeed)
            filter.sort = new FilterContestApplicationRDTO.SortContestApplicationRDTO(randomSeed: randomSeed)
        } else {
            filter.sort = new FilterContestApplicationRDTO.SortContestApplicationRDTO(field: FilterContestApplicationRDTO.ContestSortableFieldRDTO.VOTES, direction: DirectionDTO.ASC)
        }

        if (filter.sort && params.direction) {
            DirectionDTO dir = DirectionDTO.valueOf(params.direction)
            filter.sort.direction = dir
        }
        PageContestApplicationRSDTO pageContestApplications = contestService.findContestApplications(contestUser, contestId, filter, viewerUid)
        if (pageContestApplications.total == 0) {
            render template: '/contest/showModules/mainContent/contestApplicationsEmpty'
        } else {
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${pageContestApplications.total > ((pageContestApplications.page + 1) * pageContestApplications.size)}")
            render template: '/campaigns/cards/campaignsList', model: [campaigns: pageContestApplications.data, showAuthor: true]
        }
    }


    private def campaignModelContestArea(long idCampaign, ContestRSDTO contestRSDTO, ContestAreasCommand command) {
        command.setCampaignId(idCampaign)
        command.setNumWinnerApplications(contestRSDTO.getNumWinnerApplications())
        command.setMaxApplicationsPerUser(contestRSDTO.getMaxApplicationsPerUser())
        return [
                campaign: contestRSDTO,
                command : command
        ]
    }


    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def paginateContestApplicationJson() {
        initJson()
        Integer limit = Integer.parseInt(params.limit)
        Integer offset = Integer.parseInt(params.offset)
        KuorumUserSession userLogged = springSecurityService.principal
        Long contestId = Long.parseLong(params.campaignId)
        FilterContestApplicationRDTO filter = new FilterContestApplicationRDTO(page: Math.floor(offset / limit).intValue(), size: limit)
        populateFilters(filter, params.filter)
        populateSort(filter, params.sort, params.order)
        filter.attachNotPublished = true
        PageContestApplicationRSDTO pageDistrictProposals = contestService.findContestApplications(userLogged, contestId, filter, String.valueOf(userLogged.getId()))
        JSON.use('infoContestApplicationTable') {
            render(["total": pageDistrictProposals.total, "rows": pageDistrictProposals.data] as JSON)
        }
    }

    private void initJson() {
        JSON.createNamedConfig('infoContestApplicationTable') {
//            log("suggest JSON marshaled created")
            it.registerObjectMarshaller(CampaignStatusRSDTO) { CampaignStatusRSDTO status -> messageEnumJson(status) }
            it.registerObjectMarshaller(ContestApplicationActivityTypeDTO) { ContestApplicationActivityTypeDTO activityType -> messageEnumJson(activityType) }
            it.registerObjectMarshaller(ContestApplicationFocusTypeDTO) { ContestApplicationFocusTypeDTO focusType -> messageEnumJson(focusType) }
            it.registerObjectMarshaller(BasicDataKuorumUserRSDTO) { BasicDataKuorumUserRSDTO basicDataKuorumUserRSDTO ->
                [
                        id       : basicDataKuorumUserRSDTO.id,
                        alias    : basicDataKuorumUserRSDTO.alias,
                        name     : basicDataKuorumUserRSDTO.name,
                        avatarUrl: basicDataKuorumUserRSDTO.avatarUrl,
                        userLink : g.createLink(mapping: 'userShow', params: basicDataKuorumUserRSDTO.encodeAsLinkProperties())
                ]
            }
            it.registerObjectMarshaller(ContestApplicationRSDTO) { ContestApplicationRSDTO contestApplicationRSDTO ->
                [
                        id            : contestApplicationRSDTO.id,
                        name          : contestApplicationRSDTO.name,
                        title         : contestApplicationRSDTO.title,
                        body          : contestApplicationRSDTO.body,
                        campaignStatus: contestApplicationRSDTO.campaignStatusRSDTO,
                        photoUrl      : contestApplicationRSDTO.photoUrl,
                        videoUrl      : contestApplicationRSDTO.videoUrl,
                        multimediaHtml: groovyPageRenderer.render(template: '/campaigns/showModules/campaignDataMultimedia', model: [campaign: contestApplicationRSDTO]),
                        visits        : contestApplicationRSDTO.visits,
                        user          : contestApplicationRSDTO.user,
                        nid           : contestApplicationRSDTO.user.nid,
                        cause         : contestApplicationRSDTO.causes ? contestApplicationRSDTO.causes[0] : null,
                        contest       : contestApplicationRSDTO.contest,
                        activityType  : contestApplicationRSDTO.activityType,
                        focusType     : contestApplicationRSDTO.focusType,
                        url           : g.createLink(mapping: 'contestApplicationShow', params: contestApplicationRSDTO.encodeAsLinkProperties()),
                        numVotes         : contestApplicationRSDTO.votes
                ]
            }
        }
    }

    private messageEnumJson(def type) {
        [
                type: type.toString(),
                i18n: g.message(code: "${type.class.name}.${type}")
        ]
    }


    private void populateSort(FilterContestApplicationRDTO filter, String sortField, String order) {
        filter.sort = new FilterContestApplicationRDTO.SortContestApplicationRDTO()
        filter.sort.field = FilterContestApplicationRDTO.ContestSortableFieldRDTO.ID
        filter.sort.direction = DirectionDTO.ASC
        if (sortField) {
            switch (sortField) {
//                case "cause.name": filter.sort.field = FilterContestApplicationRDTO.SortableFieldRDTO.CAUSE; break
                case "user.name": filter.sort.field = FilterContestApplicationRDTO.ContestSortableFieldRDTO.CRM_USER; break
                case "numVotes": filter.sort.field = FilterContestApplicationRDTO.ContestSortableFieldRDTO.VOTES; break
                default: filter.sort.field = FilterContestApplicationRDTO.ContestSortableFieldRDTO.valueOf(sortField.toUpperCase()); break
            }
        }

        if (order) {
            filter.sort.direction = DirectionDTO.valueOf(order.toUpperCase())
        }

    }

    private void populateFilters(FilterContestApplicationRDTO filter, String jsonFilter) {
        if (jsonFilter) {
            def rawFilter = JSON.parse(jsonFilter)
            rawFilter.each { k, v -> populateFilter(filter, k, v) }
        }
    }

    private void populateFilter(FilterContestApplicationRDTO filter, String rawKey, String value) {
        switch (rawKey) {
            case "id": filter.id = Long.parseLong(value); break
            case "campaignStatus.i18n": filter.campaignStatus = CampaignStatusRSDTO.valueOf(value); break
            case "activityType.i18n": filter.activityType = ContestApplicationActivityTypeDTO.valueOf(value); break
            case "focusType.i18n": filter.focusType = ContestApplicationFocusTypeDTO.valueOf(value); break
            case "user.name": filter.userName = value; break
            case "nid": filter.nid = value; break
            default: filter[rawKey] = value; break
        }
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def updateReview(ContestApplicationChangeStatusCommand command) {
        if (command.hasErrors()) {
            render([success: false, message: "There are errors on the data"] as JSON)
        }
        Boolean isApproved = command.getNewStatus() == CampaignStatusRSDTO.APPROVED
        KuorumUserSession loggedUser = springSecurityService.principal
        contestApplicationService.updateValidate(loggedUser, command.getContestApplicationId(), isApproved)
        render([success: true, message: "Update success"] as JSON)
    }

    def sendApplicationsReport() {
        render "OK";
    }

    @Secured(['ROLE_CAMPAIGN_CONTEST'])
    def sendVotesReport() {
        KuorumUserSession campaignUser = springSecurityService.getPrincipal()
        Long contestId = Long.parseLong(params.campaignId)
        contestService.sendVotesReport(campaignUser, contestId)
        Boolean isAjax = request.xhr
        if (isAjax) {
            render([success: "success"] as JSON)
        } else {
            flash.message = g.message(code: 'modal.exportedTrackingEvents.title')
            redirect(mapping: 'contestCampaignStatsShow', params: [campaignId: contestId])
        }
    }

//    @Secured(['ROLE_CAMPAIGN_CONTEST_APPLICATION'])
    def vote(ContestApplicationVoteCommand command) {
        if (command.hasErrors()) {
            render([success: false, message: "There are errors on the data"] as JSON)
        }
        ContestRSDTO contestRSDTO = contestService.find(command.userAlias, command.contestId)
        if (!springSecurityService.isLoggedIn() && !contestRSDTO.allowAnonymousAction) {
            log.warn("Someone [NO LOGGED] trying to vote in a campaign that requires register")
            render([success: false, message: "You are unauthorized", vote: null] as JSON)
            return
        }
        KuorumUserSession loggedUser = cookieUUIDService.buildAnonymousUser()
        try {
            ContestApplicationVoteRSDTO vote = contestApplicationService.vote(command.userAlias, command.contestId, command.campaignId, loggedUser.getId().toString());
            cookieUUIDService.removeUserUUID();
            render([success: true, message: g.message(code: 'contestApplication.callToAction.VOTING.success'), vote: vote] as JSON)
        } catch (Exception e) {
            String msgError = "Error updating saving your vote"
            if (e.undeclaredThrowable.cause instanceof KuorumException) {
                KuorumException ke = e.undeclaredThrowable.cause
                String dateClosed = g.formatDate([format: g.message(code: 'default.date.format'), date: contestRSDTO.deadLineVotes, timeZone: contestRSDTO.user.timeZone])
                msgError = message(code: "contestApplication.callToAction.VOTING.${ke.errors[0].code}", args: [contestRSDTO.title, dateClosed])
            }
            cookieUUIDService.removeUserUUID();
            render([success: false, message: msgError, vote: null] as JSON)
        }
    }

    def anonymousVote(ContestApplicationVoteCommand) {

//        contestApplicationService.vote()
    }

    def ranking() {
        Long contestId = Long.parseLong(params.campaignId)
        ContestRSDTO contest = contestService.find(params.userAlias, contestId);
        [contest: contest]
    }

    def rankingContestApplicationList() {
        Long contestId = Long.parseLong(params.campaignId)
        List<ContestApplicationRSDTO> contestApplications = contestService.getRanking(params.userAlias, contestId)
        render template: '/contest/ranking/rankingCampaign', model: [contestApplications: contestApplications]
    }
}
