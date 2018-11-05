package kuorum.survey

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.survey.QuestionAnswerCommand
import kuorum.web.commands.payment.survey.QuestionCommand
import kuorum.web.commands.payment.survey.QuestionOptionCommand
import kuorum.web.commands.payment.survey.SurveyQuestionsCommand
import org.kuorum.rest.model.communication.survey.*

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
        KuorumUser surveyUser = KuorumUser.get(springSecurityService.principal.id)
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
        command.publishOn = null;
        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    private def surveyModelSettings(CampaignSettingsCommand command, SurveyRSDTO surveyRSDTO) {
        def model = modelSettings(command, surveyRSDTO)
        command.debatable=false
        model.options =[debatable:false, endDate:true]
        return model
    }

    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def editQuestionsStep(){
        Long campaignId = Long.parseLong(params.campaignId)
        KuorumUser surveyUser = KuorumUser.get(springSecurityService.principal.id)
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
        KuorumUser surveyUser = KuorumUser.get(springSecurityService.principal.id)
        SurveyRSDTO survey = surveyService.find(surveyUser, Long.parseLong(params.campaignId))
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
        rdto.questions = command.questions?.findAll{it && it.text}.collect {map(it)}?:[]
        def result = saveAndSendCampaign(surveyUser, rdto, survey.getId(), command.publishOn,command.sendType, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params

    }
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveAnswer(QuestionAnswerCommand command){
        KuorumUser userAnswer = KuorumUser.get(springSecurityService.principal.id)
        KuorumUser surveyUser = kuorumUserService.findByAlias(params.userAlias)
        SurveyRSDTO survey = surveyService.find(surveyUser, command.campaignId)
        surveyService.saveAnswer(survey, userAnswer, command.questionId, command.answersIds)
        render ([status:"success",msg:""] as JSON)
    }

    private QuestionRDTO map(QuestionCommand command){
        QuestionRDTO questionRDTO = new QuestionRDTO();
        questionRDTO.id = command.id
        questionRDTO.text = command.text
        questionRDTO.questionType = command.questionType
        questionRDTO.options = command.options?.findAll{it && it.text}.collect{mapQuestionOption(it)}?:[]
        questionRDTO
    }

    private QuestionOptionRDTO mapQuestionOption(QuestionOptionCommand command){
        QuestionOptionRDTO questionOptionRDTO = new QuestionOptionRDTO();
        questionOptionRDTO.text = command.text
        questionOptionRDTO.id = command.id
        questionOptionRDTO
    }

    private def modelQuestionStep(SurveyRSDTO survey){
        SurveyQuestionsCommand command = new SurveyQuestionsCommand()
        command.surveyId = survey.id
        command.questions = survey.questions?.collect{map(it)}?:[new QuestionCommand()]
        if(survey.datePublished){
            command.publishOn = survey.datePublished
        }
        return command
    }

    private QuestionCommand map(QuestionRSDTO questionRSDTO){
        QuestionCommand command = new QuestionCommand();
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
        return command
    }



    @Secured(['ROLE_CAMPAIGN_SURVEY'])
    def sendReport(Long campaignId){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        surveyService.sendReport(loggedUser, campaignId)
        Boolean isAjax = request.xhr
        if(isAjax){
            render ([success:"success"] as JSON)
        } else{
            flash.message = g.message(code: 'modal.exportedTrackingEvents.title')
            redirect (mapping: 'politicianCampaignStatsShow', params:[campaignId: campaignId])
        }
    }

}
