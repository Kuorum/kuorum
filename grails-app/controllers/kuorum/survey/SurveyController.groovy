package kuorum.survey

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.politician.CampaignController
import kuorum.register.KuorumUserSession
import kuorum.util.TimeZoneUtil
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.survey.*
import org.kuorum.rest.model.communication.survey.*
import org.kuorum.rest.model.communication.survey.answer.QuestionAnswerOptionRDTO
import org.kuorum.rest.model.communication.survey.answer.QuestionAnswerRDTO
import org.kuorum.rest.model.communication.survey.answer.QuestionAnswerTextRDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

class SurveyController extends CampaignController{

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def create() {
        return surveyModelSettings(new CampaignSettingsCommand(debatable:false), null)
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def remove(Long campaignId) {
        removeCampaign(campaignId, surveyService)
        render ([msg: "Survey deleted"] as JSON)
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def editSettingsStep(){
        KuorumUserSession surveyUser = springSecurityService.principal
        SurveyRSDTO surveyRSDTO = surveyService.find(surveyUser, Long.parseLong(params.campaignId))
        return surveyModelSettings(new CampaignSettingsCommand(debatable:false), surveyRSDTO)

    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def editContentStep(){
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
        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(command, params, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def saveContent(CampaignContentCommand command) {
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "post.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(Long.parseLong(params.campaignId), null, command, surveyService)
            return
        }
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        command.publishOn = null
        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    private def surveyModelSettings(CampaignSettingsCommand command, SurveyRSDTO surveyRSDTO) {
        def model = modelSettings(command, surveyRSDTO)
        command.debatable=false
        model.options =[debatable:false, hiddeVotesFlag:true]
        return model
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def editQuestionsStep(){
        Long campaignId = Long.parseLong(params.campaignId)
        KuorumUserSession surveyUser = springSecurityService.principal
        SurveyRSDTO survey = setCampaignAsDraft(campaignId, surveyService)
        if (!survey.body || !survey.title){
            flash.message=g.message(code:'survey.form.nobody.redirect')
            redirect mapping: 'surveyEditContent', params: survey.encodeAsLinkProperties()
        }else{
            Long numberRecipients = getCampaignNumberRecipients(surveyUser, survey)
            return [
                    survey:survey,
                    command: modelQuestionStep(survey),
                    status: survey.campaignStatusRSDTO,
                    numberRecipients:numberRecipients]
        }
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def saveQuestions(SurveyQuestionsCommand command){
        KuorumUserSession surveyUser = springSecurityService.principal
        SurveyRSDTO survey = surveyService.find(surveyUser, Long.parseLong(params.campaignId))

        // Clean questions deleted
        command.questions = command.questions.findAll{it}
        for (QuestionCommand questionCommand : command.questions ){
            if (questionCommand.questionType  != QuestionTypeRSDTO.TEXT_OPTION){
                // Clean all removed empty options (Text options has no answers)
                questionCommand.options = questionCommand.options.findAll{it && it.text != null}
            }
        }
        command.validate()
        if (command.hasErrors()) {
            flash.error = message(error: command.errors.getFieldError())
            render view: 'editQuestionsStep', model: [
                    survey:survey,
                    command: command,
                    status: survey.campaignStatusRSDTO,
                    numberRecipients:getCampaignNumberRecipients(surveyUser, survey)]
            return
        }
        SurveyRDTO rdto = surveyService.map(survey)
        rdto.endDate = TimeZoneUtil.convertToUserTimeZone(command.endDate, surveyUser.timeZone)
        rdto.startDate = TimeZoneUtil.convertToUserTimeZone(command.startDate, surveyUser.timeZone)
        rdto.questions = command.questions?.findAll{it && it.text}.collect {map(it)}?:[]
        def result = saveAndSendCampaign(surveyUser, rdto, survey.getId(), command.publishOn,command.sendType, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params

    }
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveAnswer(QuestionAnswerCommand command){
        command.answers = JSON.parse(params.answersJson).collect{
            new kuorum.web.commands.payment.survey.QuestionAnswerDataCommand(
                    answerId:it.answerId instanceof Number?it.answerId: Long.parseLong(it.answerId),
                    text:it.text)
        };
        command.validate();
        KuorumUserSession userAnswer = springSecurityService.principal
        BasicDataKuorumUserRSDTO surveyUser = kuorumUserService.findBasicUserRSDTO(params.userAlias)
        SurveyRSDTO survey = surveyService.find(surveyUser.id, command.campaignId)
        List<QuestionAnswerRDTO> answers = command.answers
                .collect{
                    it.text?
                            new QuestionAnswerTextRDTO([optionId:it.answerId,text:it.text]):
                            new QuestionAnswerOptionRDTO([optionId:it.answerId])
        }
        try{
            surveyService.saveAnswer(survey, userAnswer, command.questionId, answers)
        }catch(Exception e){
            if (e.cause.cause instanceof KuorumException &&  e.cause.cause.errors[0].code=="error.api.SERVICE_CAMPAIGN_NOT_EDITABLE"){
                log.info("This questions is already answered. ")
            }else{
                throw e;
            }
        }
        render ([status:"success",msg:""] as JSON)
    }

    private QuestionRDTO map(QuestionCommand command){
        QuestionRDTO questionRDTO = new QuestionRDTO()
        questionRDTO.id = command.id
        questionRDTO.text = command.text
        questionRDTO.questionType = command.questionType
        questionRDTO.options = command.options?.findAll{it && it.text}.collect{mapQuestionOption(it)}?:[]
        questionRDTO
    }

    private QuestionOptionRDTO mapQuestionOption(QuestionOptionCommand command){
        QuestionOptionRDTO questionOptionRDTO = new QuestionOptionRDTO()
        questionOptionRDTO.text = command.text
        questionOptionRDTO.id = command.id
        questionOptionRDTO.questionOptionType = command.questionOptionType
        questionOptionRDTO
    }

    private def modelQuestionStep(SurveyRSDTO survey){
        SurveyQuestionsCommand command = new SurveyQuestionsCommand()
        command.surveyId = survey.id
        command.endDate = survey.endDate
        command.startDate = survey.startDate
        command.questions = survey.questions?.collect{map(it)}?:[new QuestionCommand()]
        if(survey.datePublished){
            command.publishOn = survey.datePublished
        }
        return command
    }

    private QuestionCommand map(QuestionRSDTO questionRSDTO){
        QuestionCommand command = new QuestionCommand()
        command.id = questionRSDTO.id
        command.questionType = questionRSDTO.questionType
        command.text = questionRSDTO.text
        command.options = questionRSDTO.options.collect{map(it)}
        return command
    }

    private QuestionOptionCommand map(QuestionOptionRSDTO option){
        QuestionOptionCommand command = new QuestionOptionCommand()
        command.id = option.id
        command.text = option.text
        command.questionOptionType = option.questionOptionType
        return command
    }



    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def sendReport(SurveyReportCommand command){
        KuorumUserSession loggedUser = springSecurityService.principal
        surveyService.sendReport(loggedUser, command.campaignId,command.surveyReportType)
        Boolean isAjax = request.xhr
        if(isAjax){
            render ([success:"success"] as JSON)
        } else{
            flash.message = g.message(code: 'modal.exportedTrackingEvents.title')
            redirect (mapping: 'politicianCampaignStatsShow', params:[campaignId: campaignId])
        }
    }

}
