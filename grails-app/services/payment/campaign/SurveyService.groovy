package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.mail.KuorumMailService
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import kuorum.web.commands.payment.survey.SurveyReportType
import org.kuorum.rest.model.communication.survey.QuestionOptionRDTO
import org.kuorum.rest.model.communication.survey.QuestionRDTO
import org.kuorum.rest.model.communication.survey.SurveyRDTO
import org.kuorum.rest.model.communication.survey.SurveyRSDTO
import org.kuorum.rest.model.communication.survey.answer.QuestionAnswerRDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

@Transactional
class SurveyService implements CampaignCreatorService<SurveyRSDTO, SurveyRDTO>{

    def grailsApplication
    def indexSolrService
    def notificationService
    def fileService
    KuorumMailService kuorumMailService
    RestKuorumApiService restKuorumApiService
    CampaignService campaignService

    SurveyRSDTO save(KuorumUserSession user, SurveyRDTO surveyRDTO, Long surveyId){

        SurveyRSDTO survey = null
        if (surveyId) {
            survey= update(user, surveyRDTO, surveyId)
        } else {
            survey= createSurvey(user, surveyRDTO)
        }
        indexSolrService.deltaIndex()
        survey
    }

    private SurveyRSDTO createSurvey(KuorumUserSession user, SurveyRDTO surveyRDTO){
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEYS,
                params,
                query,
                surveyRDTO,
                new TypeReference<SurveyRSDTO>(){}
        )

        SurveyRSDTO surveySaved = null
        if (response.data) {
            surveySaved = response.data
        }

        surveySaved
    }

    List<SurveyRSDTO> findAll(KuorumUserSession user,String viewerUid = null) {
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_SURVEYS,
                    params,
                    query,
                    new TypeReference<List<SurveyRSDTO>>() {}
            )

            return response.data ?: null
        }catch (KuorumException e){
            log.info("Survey not found [Excpt: ${e.message}")
            return null
        }
    }

    SurveyRSDTO find(KuorumUserSession user, Long surveyId, String viewerUid = null){
        find(user.id.toString(), surveyId, viewerUid)
    }

    SurveyRSDTO find(String userId, Long surveyId, String viewerUid = null){
        if (!surveyId){
            return null
        }
        Map<String, String> params = [userId: userId, surveyId: surveyId.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY,
                    params,
                    query,
                    new TypeReference<SurveyRSDTO>() {}
            )

            return response.data ?: null
        }catch (KuorumException e){
            log.info("Survey not found [Excpt: ${e.message}")
            return null
        }
    }

    private SurveyRSDTO update(KuorumUserSession user, SurveyRDTO surveyRDTO, Long surveyId) {
        Map<String, String> params = [userId: user.id.toString(), surveyId: surveyId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY,
                params,
                query,
                surveyRDTO,
                new TypeReference<SurveyRSDTO>(){}
        )

        SurveyRSDTO surveySaved = null
        if (response.data) {
            surveySaved = response.data
        }
        surveySaved
    }

    void remove(KuorumUserSession user, Long surveyId) {
        Map<String, String> params = [userId: user.id.toString(), surveyId: surveyId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY,
                params,
                query
        )
        surveyId
    }

    @Override
    SurveyRDTO map(SurveyRSDTO surveyRSDTO) {
        SurveyRDTO surveyRDTO = new SurveyRDTO()
        if(surveyRSDTO){
            surveyRDTO = campaignService.basicMapping(surveyRSDTO,surveyRDTO)
            surveyRDTO.setAnonymous(surveyRSDTO.anonymous)
            surveyRDTO.setSignVotes(surveyRSDTO.signVotes)
            //MAP QUESTIONS
            surveyRDTO.questions = surveyRSDTO.questions.collect{
                QuestionRDTO questionRDTO = new QuestionRDTO()
                questionRDTO.id = it.id
                questionRDTO.text = it.text
                questionRDTO.questionType = it.questionType
                questionRDTO.maxAnswers = it.maxAnswers
                questionRDTO.minAnswers = it.minAnswers
                questionRDTO.options = it.options.collect{ qo ->
                    QuestionOptionRDTO questionOptionRDTO = new QuestionOptionRDTO()
                    questionOptionRDTO.text = qo.text
                    questionOptionRDTO.id = qo.id
                    questionOptionRDTO.questionOptionType = qo.questionOptionType
                    return questionOptionRDTO
                }
                return questionRDTO
            }
        }
        return surveyRDTO
    }

    @Override
    def buildView(SurveyRSDTO campaignRSDTO, BasicDataKuorumUserRSDTO campaignOwner, String viewerUid, def params) {
        def model = [survey: campaignRSDTO, campaignUser: campaignOwner]
        [view: "/survey/show", model:model]
    }

    void saveAnswer(SurveyRSDTO surveyRSDTO, KuorumUserSession userAnswer, Long questionId, List<QuestionAnswerRDTO> answers){
        Map<String, String> params = [userId: surveyRSDTO.user.id.toString(), surveyId: surveyRSDTO.id.toString(),questionId:questionId.toString()]
        Map<String, String> query = [viewerUid:userAnswer.id.toString()]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_ANSWER,
                params,
                query,
                answers,
                null
        )
    }

    void sendReport(KuorumUserSession user, Long surveyId, SurveyReportType surveyReportType){
        Map<String, String> params = [userId: user.id.toString(), surveyId: surveyId.toString()]
        Map<String, String> query = [:]
        RestKuorumApiService.ApiMethod apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_REPORT_STATS;
        if (SurveyReportType.SURVEY_RAW_DATA.equals(surveyReportType)){
            apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_REPORT_RAW;
        }
        def response = restKuorumApiService.get(
                apiMethod,
                params,
                query,
                null
        )
        response
    }
}
