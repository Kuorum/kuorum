package kuorum.survey

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.politician.CampaignController
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.payment.CampaignSettingsCommand
import kuorum.web.commands.payment.survey.QuestionCommand
import kuorum.web.commands.payment.survey.QuestionOptionCommand
import kuorum.web.commands.payment.survey.SurveyQuestionsCommand
import org.kuorum.rest.model.communication.survey.QuestionOptionRSDTO
import org.kuorum.rest.model.communication.survey.QuestionRSDTO
import org.kuorum.rest.model.communication.survey.SurveyRSDTO

class SurveyController extends CampaignController{

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        return surveyModelSettings(new CampaignSettingsCommand(debatable:false), null)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def remove(Long campaignId) {
        removeCampaign(campaignId)
        render ([msg: "Survey deleted"] as JSON)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editSettingsStep(){
        KuorumUser surveyUser = KuorumUser.get(springSecurityService.principal.id)
        SurveyRSDTO surveyRSDTO = surveyService.find(surveyUser, Long.parseLong(params.campaignId))
        return surveyModelSettings(new CampaignSettingsCommand(debatable:false), surveyRSDTO)

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editContentStep(){
        Long campaignId = Long.parseLong(params.campaignId)
        SurveyRSDTO surveyRSDTO = setCampaignAsDraft(campaignId, surveyService)
        return campaignModelContent(campaignId, surveyRSDTO, null, surveyService)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveSettings(CampaignSettingsCommand command) {
        if (command.hasErrors()) {
            render view: 'create', model: surveyModelSettings(command, null)
            return
        }
        command.eventAttached=false
        Map<String, Object> result = saveCampaignSettings(command, params, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def saveContent(CampaignContentCommand command) {
        if (command.hasErrors()) {
            if(command.errors.getFieldError().arguments.first() == "publishOn"){
                flash.error = message(code: "post.scheduleError")
            }
            render view: 'editContentStep', model: campaignModelContent(Long.parseLong(params.campaignId), null, command, surveyService)
            return
        }
        Long campaignId = params.campaignId?Long.parseLong(params.campaignId):null
        Map<String, Object> result = saveAndSendCampaignContent(command, campaignId, surveyService)
        redirect mapping: result.nextStep.mapping, params: result.nextStep.params
    }

    private def surveyModelSettings(CampaignSettingsCommand command, SurveyRSDTO surveyRSDTO) {
        def model = modelSettings(command, surveyRSDTO)
        command.debatable=false
        return model
    }

    def editQuestionsStep(){
        Long campaignId = Long.parseLong(params.campaignId)
        SurveyRSDTO survey = setCampaignAsDraft(campaignId, surveyService)
        [survey:survey, command: modelQuestionStep(survey)]
    }

    private def modelQuestionStep(SurveyRSDTO survey){
        SurveyQuestionsCommand command = new SurveyQuestionsCommand()
        command.surveyId = survey.id
        command.questions = survey.questions.collect{map(it)}
    }

    private QuestionCommand map(QuestionRSDTO questionRSDTO){
        QuestionCommand command = new QuestionCommand();
        command.id = questionRSDTO.id
        command.questionType = questionRSDTO.questionType
        command.text = questionRSDTO.text
        command.options = questionRSDTO.options.collect{map(it)}
    }

    private QuestionOptionCommand map(QuestionOptionRSDTO option){
        QuestionOptionCommand command = new QuestionOptionCommand()
        command.id = option.id
        command.text = option.text
    }

}
