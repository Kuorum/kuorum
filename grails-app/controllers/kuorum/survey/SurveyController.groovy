package kuorum.survey


import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.survey.*
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.survey.*
import org.kuorum.rest.model.communication.survey.answer.*
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

class SurveyController extends CampaignController {

    private List<QuestionTypeRSDTO> questionTypesWithPredefinedAnswers = [
            QuestionTypeRSDTO.CONTACT_BIRTHDATE,
            QuestionTypeRSDTO.CONTACT_EXTERNAL_ID,
            QuestionTypeRSDTO.CONTACT_GENDER,
            QuestionTypeRSDTO.CONTACT_GENDER,
            QuestionTypeRSDTO.CONTACT_PHONE,
            QuestionTypeRSDTO.CONTACT_WEIGHT,
            QuestionTypeRSDTO.RATING_OPTION,
            QuestionTypeRSDTO.CONTACT_UPLOAD_FILES,
            QuestionTypeRSDTO.TEXT_OPTION]

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def create() {
        return surveyModelSettings(new CampaignSettingsCommand(debatable: false), null)
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, surveyService)
        render([msg: "Survey deleted"] as JSON)
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def editSettingsStep() {
        KuorumUserSession surveyUser = springSecurityService.principal
        SurveyRSDTO surveyRSDTO = surveyService.find(surveyUser, Long.parseLong(params.campaignId))
        return surveyModelSettings(new CampaignSettingsCommand(debatable: false), surveyRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def editContentStep() {
        Long campaignId = Long.parseLong(params.campaignId)
        SurveyRSDTO surveyRSDTO = setCampaignAsDraft(campaignId, surveyService)
        return campaignModelContent(campaignId, surveyRSDTO, null, surveyService)
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: surveyModelSettings(command, null)
            return
        }
        command.eventAttached = false
        Map<String, Object> result = saveCampaignSettings(command, params, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def saveContent(CampaignContentCommand command) {
        if (command.hasErrors()) {
            if (command.errors.getFieldError().arguments.first() == "publishOn") {
                flash.error = message(code: "post.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(Long.parseLong(params.campaignId), null, command, surveyService)
            return
        }
        Long campaignId = params.campaignId ? Long.parseLong(params.campaignId) : null
        command.publishOn = null
        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    def copy(Long campaignId) {
        return super.copyCampaign(campaignId, surveyService)
    }


    private def surveyModelSettings(CampaignSettingsCommand command, SurveyRSDTO surveyRSDTO) {
        def model = modelSettings(command, surveyRSDTO)
        command.debatable = false
        if (surveyRSDTO) {
            command.voteType = surveyRSDTO.getVoteType()
            command.signVotes = surveyRSDTO.getSignVotes()
        }
        model.options = [debatable: false, showCampaignDateLimits: true, showSurveyCustomFields: true]
        return model
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def editQuestionsStep() {
        Long campaignId = Long.parseLong(params.campaignId)
        KuorumUserSession surveyUser = springSecurityService.principal
        SurveyRSDTO survey = setCampaignAsDraft(campaignId, surveyService)
        if (!survey.body || !survey.title) {
            flash.error = g.message(code: 'survey.form.nobody.redirect')
            redirect mapping: 'surveyEditContent', params: survey.encodeAsLinkProperties()
        } else {
            Long numberRecipients = getCampaignNumberRecipients(surveyUser, survey)
            return [
                    survey          : survey,
                    command         : modelQuestionStep(survey),
                    status          : survey.campaignStatusRSDTO,
                    numberRecipients: numberRecipients]
        }
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def saveQuestions(SurveyQuestionsCommand command) {
        KuorumUserSession surveyUser = springSecurityService.principal
        SurveyRSDTO survey = surveyService.find(surveyUser, Long.parseLong(params.campaignId))

        // Clean questions deleted
        command.questions = command.questions.findAll { it }
        for (QuestionCommand questionCommand : command.questions) {
            if (!questionTypesWithPredefinedAnswers.contains(questionCommand.questionType)) {
                // Clean all removed empty options (Questions with predefined answers has no answers)
                questionCommand.options = questionCommand.options.findAll { it && it.text != null }
            }
        }
        command.validate()
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: 'editQuestionsStep', model: [
                    survey          : survey,
                    command         : command,
                    status          : survey.campaignStatusRSDTO,
                    numberRecipients: getCampaignNumberRecipients(surveyUser, survey)]
            return
        }
        SurveyRDTO rdto = surveyService.map(survey)
        rdto.questions = command.questions?.findAll { it && it.text }.collect { mapQuestion(it) } ?: []
        def result = saveAndSendCampaign(surveyUser, rdto, survey.getId(), command.publishOn, command.sendType, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveAnswer(QuestionAnswerCommand command) {
        command.answers = JSON.parse(params.answersJson).collect {
            new kuorum.web.commands.payment.survey.QuestionAnswerDataCommand(
                    questionOptionType: QuestionOptionTypeRDTO.valueOf(it.questionOptionType),
                    answerId: it.answerId instanceof Number ? it.answerId : Long.parseLong(it.answerId),
                    number: it.data.number == null || it.data.number instanceof Number ? it.data.number : Double.parseDouble(it.data.number),
                    date: it.data.date == null || it.data.date instanceof Date ? it.data.date : Date.parse(WebConstants.WEB_FORMAT_DATE_SHORT, it.data.date),
                    text: it.data.text,
                    text2: it.data.text2,)
        };
        command.validate();
        KuorumUserSession userAnswer = springSecurityService.principal
        BasicDataKuorumUserRSDTO surveyUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        SurveyRSDTO survey = surveyService.find(surveyUser.id, command.campaignId)
        List<QuestionAnswerRDTO> answers = command.answers.collect { convertToRDTO(it) }
        QuestionRSDTO questionRSDTO
        try {
            questionRSDTO = surveyService.saveAnswer(survey, userAnswer, command.questionId, answers)
        } catch (Exception e) {
            if (e.cause.cause instanceof KuorumException && e.cause.cause.errors[0].code == "error.api.SERVICE_CAMPAIGN_NOT_EDITABLE") {
                log.info("This questions is already answered. ")
                questionRSDTO = null
//                render(contentType: "text/json") { response ERROR: [code: 403, msg: "Access Denied."] }
            } else if (e.cause.cause instanceof KuorumException && e.cause.cause.errors[0].code == "error.api.SERVICE_UNAUTHORIZED") {
                log.info("The user is not authorized. Probably admin changed campaign validation and user didn't reload. ")
                response.status = 403
                render([status: "403", msg: g.message(code: 'kuorum.web.commands.payment.survey.QuestionAnswerCommand.failValidation')] as JSON)
                return;
            } else if (e.cause.cause instanceof KuorumException && e.cause.cause.errors[0].code == "error.api.SERVICE_CAMPAIGN_CLOSED") {
                log.info("The survey is closed. The user is not allowed to add new answers")
                response.status = 405
                render([status: "405", msg: g.message(code: 'kuorum.web.commands.payment.survey.QuestionAnswerCommand.surveyClosed')] as JSON)
                return;
            } else if (e.cause.cause instanceof KuorumException && e.cause.cause.errors[0].code == "error.api.SERVICE_SURVEY_WRONG_QUESTIONS") {
                String errorMsg = e.cause.cause.message;
                log.info("Error saving the answer. API responds error: ${errorMsg}")
                response.status = 405
                render([status: "405", msg: errorMsg] as JSON)
                return;
            } else {
                log.info("Error saving the answer")
                response.status = 500
                render([status: "500", msg: g.message(code: 'kuorum.web.commands.payment.survey.QuestionAnswerCommand.genericError')] as JSON)
                return;
            }
        }
        render([status: "success", msg: "", question: questionRSDTO] as JSON)
    }

    QuestionAnswerRDTO convertToRDTO(QuestionAnswerDataCommand qac) {
        switch (qac.questionOptionType) {
            case QuestionOptionTypeRDTO.ANSWER_TEXT:
                return new QuestionAnswerTextRDTO([optionId: qac.answerId, text: qac.text])
            case QuestionOptionTypeRDTO.ANSWER_SMALL_TEXT:
                return new QuestionAnswerSmallTextRDTO([optionId: qac.answerId, text: qac.text])
            case QuestionOptionTypeRDTO.ANSWER_PREDEFINED:
                return new QuestionAnswerPredefinedRDTO([optionId: qac.answerId])
            case QuestionOptionTypeRDTO.ANSWER_DATE:
                return new QuestionAnswerDateRDTO([optionId: qac.answerId, date: qac.date])
            case QuestionOptionTypeRDTO.ANSWER_PHONE:
                return new QuestionAnswerPhoneRDTO([optionId: qac.answerId, phone: qac.text, prefixPhone: qac.text2])
            case QuestionOptionTypeRDTO.ANSWER_NUMBER:
                return new QuestionAnswerNumberRDTO([optionId: qac.answerId, number: qac.number])
            case QuestionOptionTypeRDTO.ANSWER_FILES:
                return new QuestionAnswerFilesRDTO([optionId: qac.answerId])
            default:
                return null;
        }
    }

    private QuestionRDTO mapQuestion(QuestionCommand command) {
        QuestionRDTO questionRDTO = new QuestionRDTO()
        questionRDTO.id = command.id
        questionRDTO.text = command.text
        questionRDTO.questionType = command.questionType
        questionRDTO.points = command.points ?: 0D;
        if (command.questionLimitAnswersType == QuestionLimitAnswersType.RANGE) {
            questionRDTO.minAnswers = command.minAnswers;
            questionRDTO.maxAnswers = command.maxAnswers
        } else if (command.questionLimitAnswersType == QuestionLimitAnswersType.FORCE) {
            questionRDTO.minAnswers = command.maxAnswers;
            questionRDTO.maxAnswers = command.maxAnswers
        } else if (command.questionLimitAnswersType == QuestionLimitAnswersType.MAX) {
            questionRDTO.minAnswers = 1
            questionRDTO.maxAnswers = command.maxAnswers
        } else if (command.questionLimitAnswersType == QuestionLimitAnswersType.MIN) {
            questionRDTO.minAnswers = command.minAnswers
            questionRDTO.maxAnswers = 0
        }
        questionRDTO.options = command.options?.findAll { it && it.text }.collect { mapQuestionOption(it) } ?: []
        questionRDTO
    }

    private QuestionOptionRDTO mapQuestionOption(QuestionOptionCommand command) {
        QuestionOptionRDTO questionOptionRDTO = new QuestionOptionRDTO()
        questionOptionRDTO.text = command.text
        questionOptionRDTO.id = command.id
        questionOptionRDTO.questionOptionType = command.questionOptionType
        questionOptionRDTO.nextQuestionId = command.nextQuestionId > 0 ? command.nextQuestionId : null
        questionOptionRDTO.exitSurvey = command.nextQuestionId == 0 ? true : false
        questionOptionRDTO
    }

    private def modelQuestionStep(SurveyRSDTO survey) {
        SurveyQuestionsCommand command = new SurveyQuestionsCommand()
        command.surveyId = survey.id
        command.questions = survey.questions?.collect { mapQuestion(it) } ?: [new QuestionCommand()]
        if (survey.datePublished) {
            command.publishOn = survey.datePublished
        }
        return command
    }

    private QuestionCommand mapQuestion(QuestionRSDTO questionRSDTO) {
        QuestionCommand command = new QuestionCommand()
        command.id = questionRSDTO.id
        command.questionType = questionRSDTO.questionType
        command.text = questionRSDTO.text
        command.maxAnswers = questionRSDTO.maxAnswers
        command.minAnswers = questionRSDTO.minAnswers
        command.points = questionRSDTO.points
        command.questionLimitAnswersType = QuestionLimitAnswersType.inferType(questionRSDTO)
        command.options = questionRSDTO.options.collect { mapQuestionOption(it) }
        return command
    }

    private QuestionOptionCommand mapQuestionOption(QuestionOptionRSDTO option) {
        QuestionOptionCommand command = new QuestionOptionCommand()
        command.id = option.id
        command.text = option.text
        command.questionOptionType = option.questionOptionType
        command.nextQuestionId = option.exitSurvey ? 0 : option.nextQuestionId
        return command
    }


    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def sendReport(SurveyReportCommand command) {
        KuorumUserSession loggedUser = springSecurityService.principal
        surveyService.sendReport(loggedUser, command.campaignId, command.surveyReportType, command.pdfFormat)
        Boolean isAjax = request.xhr
        if (isAjax) {
            render([success: "success"] as JSON)
        } else {
            flash.message = g.message(code: 'modal.exportedTrackingEvents.title')
            redirect(mapping: 'politicianCampaignStatsShow', params: [campaignId: campaignId])
        }
    }

}
