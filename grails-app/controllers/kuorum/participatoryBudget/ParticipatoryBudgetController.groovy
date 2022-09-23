package kuorum.participatoryBudget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.participatoryBudget.*
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.CampaignLightRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.*
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.search.DirectionDTO

import java.lang.reflect.UndeclaredThrowableException

import static kuorum.util.rest.RestKuorumApiService.ApiMethod.ACCOUNT_PARTICIPATORY_BUDGET_REPORT

class ParticipatoryBudgetController extends CampaignController {

    // Grails renderer -> For CSV hack
    grails.gsp.PageRenderer groovyPageRenderer

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def create() {
        return participatoryBudgetModelSettings(new CampaignSettingsCommand(debatable: true), null)
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def editSettingsStep() {
        KuorumUserSession user = springSecurityService.principal
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(user, Long.parseLong((String) params.campaignId))

        return participatoryBudgetModelSettings(new CampaignSettingsCommand(debatable: true), participatoryBudgetRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def editContentStep() {
        Long campaignId = Long.parseLong((String) params.campaignId)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = setCampaignAsDraft(campaignId, participatoryBudgetService)
        return campaignModelContent(campaignId, participatoryBudgetRSDTO, null, participatoryBudgetService)
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: participatoryBudgetModelSettings(command, null)
            return
        }

        command.eventAttached = false
        Map<String, Object> result = saveCampaignSettings(command, params, participatoryBudgetService)

        //flash.message = resultDebate.msg.toString()
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def saveContent(CampaignContentCommand command) {
        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        if (command.hasErrors()) {
            if (command.errors.getFieldError().arguments.first() == "publishOn") {
                flash.error = message(code: "debate.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(campaignId, null, command, participatoryBudgetService)
            return
        }

        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, participatoryBudgetService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_DISTRICT_PROPOSAL', 'ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def listActiveParticipativeBudgets() {
        ParticipatoryBudgetStatusDTO budgetStatusDTO = ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS
        List<CampaignLightRSDTO> listParticipatoryBudgetRSDTO = participatoryBudgetService.findActiveParticipatoryBudgets(budgetStatusDTO)
        render template: '/participatoryBudget/modalParticipatoryBudgets', model: [pbList: listParticipatoryBudgetRSDTO]
    }


    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def editDeadlines() {
        Long campaignId = Long.parseLong(params.campaignId)
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = setCampaignAsDraft(campaignId, participatoryBudgetService)
        if (!participatoryBudgetRSDTO.body || !participatoryBudgetRSDTO.title) {
            flash.message = g.message(code: 'participatoryBudget.form.nobody.redirect')
            redirect mapping: 'participatoryBudgetEditContent', params: participatoryBudgetRSDTO.encodeAsLinkProperties()
        } else {
            return [
                    campaign: participatoryBudgetRSDTO,
                    command : buildCommandDeadlinesStep(participatoryBudgetRSDTO)
            ]
        }
    }

    private ParticipatoryBudgetDeadlinesCommand buildCommandDeadlinesStep(ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {

        new ParticipatoryBudgetDeadlinesCommand(
                campaignId: participatoryBudgetRSDTO.id,
                deadLineProposals: participatoryBudgetRSDTO.deadLineProposals,
                deadLineTechnicalReview: participatoryBudgetRSDTO.deadLineTechnicalReview,
                deadLineVotes: participatoryBudgetRSDTO.deadLineVotes,
                deadLineFinalReview: participatoryBudgetRSDTO.deadLineFinalReview
        )
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def saveDeadlines(ParticipatoryBudgetDeadlinesCommand command) {
        KuorumUserSession campaignUser = springSecurityService.principal
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(campaignUser, command.campaignId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: 'editDeadlines', model: [
                    campaign: participatoryBudgetRSDTO,
                    command : command]
            return
        }
        ParticipatoryBudgetRDTO rdto = participatoryBudgetService.map(participatoryBudgetRSDTO)
        rdto.deadLineProposals = command.deadLineProposals
        rdto.deadLineTechnicalReview = command.deadLineTechnicalReview
        rdto.deadLineVotes = command.deadLineVotes
        rdto.deadLineFinalReview = command.deadLineFinalReview
        def result = saveAndSendCampaign(campaignUser, rdto, participatoryBudgetRSDTO.getId(), null, null, participatoryBudgetService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def editDistricts() {
        Long campaignId = Long.parseLong(params.campaignId)
        KuorumUserSession campaignUser = springSecurityService.principal
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = setCampaignAsDraft(campaignId, participatoryBudgetService)
        if (!participatoryBudgetRSDTO.body || !participatoryBudgetRSDTO.title) {
            flash.message = g.message(code: 'participatoryBudget.form.nobody.redirect')
            redirect mapping: 'participatoryBudgetEditContent', params: participatoryBudgetRSDTO.encodeAsLinkProperties()
        } else if (
                !participatoryBudgetRSDTO.deadLineTechnicalReview ||
                        !participatoryBudgetRSDTO.deadLineProposals ||
                        !participatoryBudgetRSDTO.deadLineVotes
        ) {
            flash.message = g.message(code: 'participatoryBudget.form.nobody.redirect')
            redirect mapping: 'participatoryBudgetEditDeadlines', params: participatoryBudgetRSDTO.encodeAsLinkProperties()
        } else {
            Long numberRecipients = getCampaignNumberRecipients(campaignUser, participatoryBudgetRSDTO)
            return [
                    campaign        : participatoryBudgetRSDTO,
                    command         : modelDistrictsStep(participatoryBudgetRSDTO),
                    numberRecipients: numberRecipients]
        }
    }

    private def modelDistrictsStep(ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {
        def districts
        if (participatoryBudgetRSDTO.districts) {
            districts = participatoryBudgetRSDTO.districts.collect { d -> new DistrictCommand(allCity: d.allCity, name: d.name, budget: d.budget, districtId: d.id, minVotesImplementProposals: d.minVotesImplementProposals, maxEligibleProposals: d.maxEligibleProposals) }
        } else {
            districts = [new DistrictCommand()]
        }

        new DistrictsCommand(
                districts: districts,
                maxDistrictProposalsPerUser: participatoryBudgetRSDTO.maxDistrictProposalsPerUser,
                activeSupport: participatoryBudgetRSDTO.activeSupport,
                addProposalsWithValidation: participatoryBudgetRSDTO.addProposalsWithValidation,
                participatoryBudgetType: participatoryBudgetRSDTO.participatoryBudgetType
        )
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def saveDistricts(DistrictsCommand command) {
        KuorumUserSession campaignUser = springSecurityService.principal
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(campaignUser, command.campaignId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: 'editDistricts', model: [
                    campaign        : participatoryBudgetRSDTO,
                    command         : command,
                    numberRecipients: getCampaignNumberRecipients(campaignUser, participatoryBudgetRSDTO)]
            return
        }
        ParticipatoryBudgetRDTO rdto = participatoryBudgetService.map(participatoryBudgetRSDTO)
        rdto.districts = command.districts?.findAll { it && it.name && it.budget }.collect { mapDistrict(it) } ?: []
        rdto.maxDistrictProposalsPerUser = command.maxDistrictProposalsPerUser
        rdto.activeSupport = command.activeSupport == null ? false : true
        rdto.addProposalsWithValidation = command.addProposalsWithValidation == null ? false : true
        rdto.participatoryBudgetType = command.participatoryBudgetType
        def result = saveAndSendCampaign(campaignUser, rdto, participatoryBudgetRSDTO.getId(), command.publishOn, command.sendType, participatoryBudgetService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    private DistrictRDTO mapDistrict(DistrictCommand districtCommand) {
        new DistrictRDTO(
                id: districtCommand.districtId,
                name: districtCommand.name,
                budget: districtCommand.budget,
                minVotesImplementProposals: districtCommand.minVotesImplementProposals,
                maxEligibleProposals: districtCommand.maxEligibleProposals,
                allCity: districtCommand.allCity ?: false
        )
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, participatoryBudgetService)
        render([msg: "Participatory budget deleted"] as JSON)
    }

    private def participatoryBudgetModelSettings(CampaignSettingsCommand command, ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {
        def model = modelSettings(command, participatoryBudgetRSDTO)
        command.debatable = false
        model.options = [debatable: false]
        return model
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def editStatus(ParticipatoryBudgetChangeStatusCommand command) {
        KuorumUserSession campaignUser = springSecurityService.principal
        ParticipatoryBudgetRSDTO participatoryBudgetRSDTO = participatoryBudgetService.find(campaignUser, command.campaignId)
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            redirect mapping: 'campaignShow', params: participatoryBudgetRSDTO.encodeAsLinkProperties()
            return
        }
        ParticipatoryBudgetRDTO rdto = participatoryBudgetService.map(participatoryBudgetRSDTO)
        rdto.setStatus(command.getStatus())
        String msgError = null
        try {
            participatoryBudgetService.save(campaignUser, rdto, command.getCampaignId())
        } catch (UndeclaredThrowableException e) {
            if (e.undeclaredThrowable.cause instanceof KuorumException) {
                KuorumException ke = e.undeclaredThrowable.cause
                msgError = message(code: ke.errors[0].code)
            } else {
                msgError = "Error updating participatory budget status"
            }
        }
        if (request.xhr) {
            render([success: (msgError == null), msg: msgError] as JSON)
        } else {
            if (msgError) {
                flash.error = msgError
            }
            redirect mapping: 'campaignShow', params: participatoryBudgetRSDTO.encodeAsLinkProperties()
        }
    }

    def findDistrictProposals() {
        BasicDataKuorumUserRSDTO districtProposalUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        Long participatoryBudgetId = Long.parseLong(params.campaignId)
        Long districtId = Long.parseLong(params.districtId)
        Integer page = params.page ? Integer.parseInt(params.page) : 0
        String viewerUid = cookieUUIDService.buildUserUUID()
        FilterDistrictProposalRDTO filter = new FilterDistrictProposalRDTO(districtId: districtId, page: page)
        if (params.randomSeed) {
            Double randomSeed = Double.parseDouble(params.randomSeed)
            filter.sort = new FilterDistrictProposalRDTO.SortDistrictProposalRDTO(randomSeed: randomSeed)
        } else {
            filter.sort = new FilterDistrictProposalRDTO.SortDistrictProposalRDTO(field: FilterDistrictProposalRDTO.SortableFieldRDTO.PRICE, direction: DirectionDTO.ASC)
        }
        ParticipatoryBudgetStatusDTO participatoryBudgetStatus = ParticipatoryBudgetStatusDTO.valueOf(params.participatoryBudgetStatus)
        switch (participatoryBudgetStatus) {
            case ParticipatoryBudgetStatusDTO.RESULTS:
                filter.sort = new FilterDistrictProposalRDTO.SortDistrictProposalRDTO(field: FilterDistrictProposalRDTO.SortableFieldRDTO.VOTES, direction: DirectionDTO.DESC)
                filter.approved = true
                break
            case ParticipatoryBudgetStatusDTO.BALLOT:
            case ParticipatoryBudgetStatusDTO.CLOSED:
                filter.approved = true
                break
            case ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS:
            case ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW:
            default:
                break
        }

        if (filter.sort && params.direction) {
            DirectionDTO dir = DirectionDTO.valueOf(params.direction)
            filter.sort.direction = dir
        }
        PageDistrictProposalRSDTO pageDistrictProposals = participatoryBudgetService.findDistrictProposalsByDistrict(districtProposalUser, participatoryBudgetId, filter, viewerUid)
        if (pageDistrictProposals.total == 0) {
            render template: '/participatoryBudget/showModules/mainContent/districProposalsEmpty'
        } else {
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${pageDistrictProposals.total > ((pageDistrictProposals.page + 1) * pageDistrictProposals.size)}")
            render template: '/campaigns/cards/campaignsList', model: [campaigns: pageDistrictProposals.data, showAuthor: true]
        }
    }


    /******************/
    /***** END CRUD ***/
    /******************/

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def sendProposalsReport() {
        sendReport(ACCOUNT_PARTICIPATORY_BUDGET_REPORT)
    }


    private messageEnumJson(def type) {
        [
                type: type.toString(),
                i18n: g.message(code: "${type.class.name}.${type}")
        ]
    }

    void init() {
        JSON.createNamedConfig('infoDistrictProposalTable') {
//            log("suggest JSON marshaled created")
            it.registerObjectMarshaller(ParticipatoryBudgetStatusDTO) { ParticipatoryBudgetStatusDTO status -> messageEnumJson(status) }
            it.registerObjectMarshaller(TechnicalReviewStatusRDTO) { TechnicalReviewStatusRDTO status -> messageEnumJson(status) }
            it.registerObjectMarshaller(BasicDataKuorumUserRSDTO) { BasicDataKuorumUserRSDTO basicDataKuorumUserRSDTO ->
                [
                        id       : basicDataKuorumUserRSDTO.id,
                        alias    : basicDataKuorumUserRSDTO.alias,
                        name     : basicDataKuorumUserRSDTO.name,
                        avatarUrl: basicDataKuorumUserRSDTO.avatarUrl,
                        userLink : g.createLink(mapping: 'userShow', params: basicDataKuorumUserRSDTO.encodeAsLinkProperties())
                ]
            }
            it.registerObjectMarshaller(DistrictProposalRSDTO) { DistrictProposalRSDTO districtProposalRSDTO ->
                [
                        id                   : districtProposalRSDTO.id,
                        name                 : districtProposalRSDTO.name,
                        title                : districtProposalRSDTO.title,
                        body                 : districtProposalRSDTO.body,
                        photoUrl             : districtProposalRSDTO.photoUrl,
                        videoUrl             : districtProposalRSDTO.videoUrl,
                        multimediaHtml       : groovyPageRenderer.render(template: '/campaigns/showModules/campaignDataMultimedia', model: [campaign: districtProposalRSDTO]),
                        visits               : districtProposalRSDTO.visits,
                        user                 : districtProposalRSDTO.user,
                        cause                : districtProposalRSDTO.causes ? districtProposalRSDTO.causes[0] : null,
                        participatoryBudget  : districtProposalRSDTO.participatoryBudget,
                        district             : districtProposalRSDTO.district,
                        participatoryBudget  : districtProposalRSDTO.participatoryBudget,
                        district             : districtProposalRSDTO.district,
                        approved             : districtProposalRSDTO.approved,
                        price                : districtProposalRSDTO.price,
                        rejectComment        : districtProposalRSDTO.rejectComment,
                        implemented          : districtProposalRSDTO.implemented,
                        technicalReviewStatus: districtProposalRSDTO.technicalReviewStatus,
                        numSupports          : districtProposalRSDTO.numSupports,
                        numVotes             : districtProposalRSDTO.numVotes,
                        backerType           : g.message(code: "org.kuorum.rest.model.communication.participatoryBudget.BackerTypeRSDTO.${districtProposalRSDTO.backerType}"),
                        url                  : g.createLink(mapping: 'districtProposalShow', params: districtProposalRSDTO.encodeAsLinkProperties())
                ]
            }
        }
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def paginateParticipatoryBudgetProposalsJson() {
        init()
        Integer limit = Integer.parseInt(params.limit)
        Integer offset = Integer.parseInt(params.offset)
        KuorumUserSession userLogged = springSecurityService.principal
        Long participatoryBudgetId = Long.parseLong(params.campaignId)
        FilterDistrictProposalRDTO filter = new FilterDistrictProposalRDTO(page: Math.floor(offset / limit).intValue(), size: limit)
        populateFilters(filter, params.filter)
        populateSort(filter, params.sort, params.order)
        PageDistrictProposalRSDTO pageDistrictProposals = participatoryBudgetService.findDistrictProposalsByDistrict(userLogged, participatoryBudgetId, filter)
//        response.setContentType("application/json")
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
//        def data = pageDistrictProposals.data.collect{
//            def props = it.getProperties().collectEntries{ property ->
////                [(property): it."${property}"]
//                [(property.key): property.value]
//            }
//            props.put('multimediaHtml', groovyPageRenderer.render(template: '/campaigns/showModules/campaignDataMultimedia', model: [campaign:it]))
//            return props;
//        }
//        pageDistrictProposals.data.each {it.metaClass.youtubeHtml = "HTML"}
        JSON.use('infoDistrictProposalTable') {
            render(["total": pageDistrictProposals.total, "rows": pageDistrictProposals.data] as JSON)
        }
    }


    private void populateSort(FilterDistrictProposalRDTO filter, String sortField, String order) {
        filter.sort = new FilterDistrictProposalRDTO.SortDistrictProposalRDTO()
        filter.sort.field = FilterDistrictProposalRDTO.SortableFieldRDTO.ID
        filter.sort.direction = DirectionDTO.ASC
        if (sortField) {
            switch (sortField) {
                case "district.name": filter.sort.field = FilterDistrictProposalRDTO.SortableFieldRDTO.DISTRICT; break
                case "user.name": filter.sort.field = FilterDistrictProposalRDTO.SortableFieldRDTO.CRM_USER; break
                case "numSupports": filter.sort.field = FilterDistrictProposalRDTO.SortableFieldRDTO.SUPPORTS; break
                case "numVotes": filter.sort.field = FilterDistrictProposalRDTO.SortableFieldRDTO.VOTES; break
                default: filter.sort.field = FilterDistrictProposalRDTO.SortableFieldRDTO.valueOf(sortField.toUpperCase()); break
            }
        }

        if (order) {
            filter.sort.direction = DirectionDTO.valueOf(order.toUpperCase())
        }

    }

    private void populateFilters(FilterDistrictProposalRDTO filter, String jsonFilter) {
        if (jsonFilter) {
            def rawFilter = JSON.parse(jsonFilter)
            rawFilter.each { k, v -> populateFilter(filter, k, v) }
        }
    }

    private void populateFilter(FilterDistrictProposalRDTO filter, String rawKey, String value) {
        switch (rawKey) {
            case "district.name": filter.districtId = Long.parseLong(value); break
            case "id": filter.id = Long.parseLong(value); break
            case "approved": filter.approved = Boolean.parseBoolean(value); break
            case "implemented": filter.implemented = Boolean.parseBoolean(value); break
            case "user.name": filter.userName = value; break
            case "technicalReviewStatus.i18n": filter.technicalReviewStatus = TechnicalReviewStatusRDTO.valueOf(value); break
            case "backerType": filter.backerType = BackerTypeRSDTO.valueOf(value); break
            default: filter[rawKey] = value; break
        }
    }

    @Secured(['ROLE_CAMPAIGN_PARTICIPATORY_BUDGET'])
    def updateTechnicalReview(DistrictProposalTechnicalReviewCommand command) {
        init()
        KuorumUserSession campaignUser = springSecurityService.principal

        DistrictProposalTechnicalReviewRDTO technicalReviewRDTO = new DistrictProposalTechnicalReviewRDTO()
        technicalReviewRDTO.approved = command.approved
        technicalReviewRDTO.price = command.price
        technicalReviewRDTO.rejectComment = command.rejectComment
        try {
            DistrictProposalRSDTO districtProposalRSDTO = districtProposalService.technicalReview(campaignUser, command.participatoryBudgetId, command.districtProposalId, technicalReviewRDTO)
            JSON.use('infoDistrictProposalTable') {
                render([success: true, msg: null, districtProposalData: districtProposalRSDTO] as JSON)
            }
        } catch (Exception e) {
            DistrictProposalRSDTO districtProposalRSDTO = districtProposalService.find(command.districtProposalUserId, command.districtProposalId)
            Exception realCause = e?.cause?.cause ?: null
            String msg = "Error updating proposal"
            if (realCause && realCause instanceof kuorum.core.exception.KuorumException) {
                msg = g.message(code: ((kuorum.core.exception.KuorumException) realCause).errors.get(0).code)
                districtProposalRSDTO.setPrice(command.price?.intValue() ?: null)
                districtProposalRSDTO.setTechnicalReviewStatus(TechnicalReviewStatusRDTO.INCORRECT)
                response.status = 420
            } else {
                response.status = 500
            }
            JSON.use('infoDistrictProposalTable') {
                render([success: false, msg: msg, districtProposalData: districtProposalRSDTO] as JSON)
            }
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def supportDistrictProposal(DistrictProposalVoteCommand command) {
        if (command.hasErrors()) {
            render "No correct data"
            return
        }
        KuorumUserSession currentUser = springSecurityService.principal
        BasicDataKuorumUserRSDTO participatoryBudgetUser = kuorumUserService.findBasicUserRSDTO(command.getUserAlias())
        DistrictProposalRSDTO districtProposalRSDTO
        try {
            if (command.vote) {
                districtProposalRSDTO = districtProposalService.support(currentUser, participatoryBudgetUser, command.participatoryBudgetId, command.proposalId)
            } else {
                districtProposalRSDTO = districtProposalService.unsupport(currentUser, participatoryBudgetUser, command.participatoryBudgetId, command.proposalId)
            }
            render(districtProposalRSDTO as JSON)
        } catch (Exception e) {
            response.status = 500
            if (e instanceof UndeclaredThrowableException) {
                KuorumException ke = ((UndeclaredThrowableException) e).getCause().getCause()
                render "{\"error\": \"API_ERROR\", \"code\":\"${ke.errors[0].code}\", \"msg\":\"${g.message(code: 'participatoryBudget.district.modal.differentDistrict.text', args: ke.errors[0].args)}\"}"
            } else {
                render "{\"error\": \"GENERIC_ERROR\", \"code\":\"error.api.500\"}"
            }
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def voteDistrictProposal(DistrictProposalVoteCommand command) {
        if (command.hasErrors()) {
            render "No correct data"
        }
        KuorumUserSession currentUser = springSecurityService.principal
        BasicDataKuorumUserRSDTO participatoryBudgetUser = kuorumUserService.findBasicUserRSDTO(command.getUserAlias())
        DistrictProposalRSDTO districtProposalRSDTO
        try {
            if (command.vote) {
                districtProposalRSDTO = districtProposalService.vote(currentUser, participatoryBudgetUser, command.participatoryBudgetId, command.proposalId)
            } else {
                districtProposalRSDTO = districtProposalService.unvote(currentUser, participatoryBudgetUser, command.participatoryBudgetId, command.proposalId)
            }
            render(districtProposalRSDTO as JSON)
        } catch (Exception e) {
            response.status = 500
            if (e instanceof UndeclaredThrowableException) {
                KuorumException ke = ((UndeclaredThrowableException) e).getCause().getCause()
                log.info("Error voting: [Kuorum Exception] " + ke.getMessage());
                render "{\"error\": \"API_ERROR\", \"code\":\"${ke.errors[0].code}\", \"msg\":\"${g.message(code: 'participatoryBudget.district.modal.differentDistrict.text', args: ke.errors[0].args)}\"}"
            } else {
                log.info("Error voting: [Generic Exception]" + e.getMessage());
                render "{\"error\": \"GENERIC_ERROR\", \"code\":\"error.api.500\"}"
            }
        }
    }

    def copy(Long campaignId) {
        return super.copyCampaign(campaignId, participatoryBudgetService)
    }

}
