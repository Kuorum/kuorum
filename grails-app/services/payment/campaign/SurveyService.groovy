package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.mail.KuorumMailService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.survey.QuestionOptionRDTO
import org.kuorum.rest.model.communication.survey.QuestionRDTO
import org.kuorum.rest.model.communication.survey.SurveyRDTO
import org.kuorum.rest.model.communication.survey.SurveyRSDTO

@Transactional
class SurveyService implements CampaignCreatorService<SurveyRSDTO, SurveyRDTO>{

    def grailsApplication
    def indexSolrService
    def notificationService
    def fileService
    KuorumMailService kuorumMailService
    RestKuorumApiService restKuorumApiService
    CampaignService campaignService

    SurveyRSDTO save(KuorumUser user, SurveyRDTO surveyRDTO, Long surveyId){

        SurveyRSDTO survey = null;
        if (surveyId) {
            survey= update(user, surveyRDTO, surveyId)
        } else {
            survey= createSurvey(user, surveyRDTO)
        }
        indexSolrService.deltaIndex();
        survey
    }

    SurveyRSDTO createSurvey(KuorumUser user, SurveyRDTO surveyRDTO){
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

    SurveyRSDTO find(KuorumUser user, Long surveyId, String viewerUid = null){
        if (!surveyId){
            return null;
        }
        Map<String, String> params = [userId: user.id.toString(), surveyId: surveyId.toString()]
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

            return response.data ?: null;
        }catch (KuorumException e){
            log.info("Survey not found [Excpt: ${e.message}")
            return null;
        }
    }

    SurveyRSDTO update(KuorumUser user, SurveyRDTO surveyRDTO, Long surveyId) {
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

    void remove(KuorumUser user, Long surveyId) {
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
            //MAP QUESTIONS
            surveyRDTO.questions = surveyRSDTO.questions.collect{
                QuestionRDTO questionRDTO = new QuestionRDTO();
                questionRDTO.id = it.id
                questionRDTO.text = it.text
                questionRDTO.questionType = it.questionType
                questionRDTO.options = it.options.collect{ qo ->
                    QuestionOptionRDTO questionOptionRDTO = new QuestionOptionRDTO()
                    questionOptionRDTO.text = qo.text
                    questionOptionRDTO.id = qo.id
                    return questionOptionRDTO
                }
                return questionRDTO
            }
        }
        return surveyRDTO;
    }

    @Override
    def buildView(SurveyRSDTO campaignRSDTO, KuorumUser campaignOwner, String viewerUid, def params) {
        def model = [survey: campaignRSDTO, campaignUser: campaignOwner]
        [view: "/survey/show", model:model]
    }

    void saveAnswer(SurveyRSDTO surveyRSDTO, KuorumUser userAnswer, Long questionId, List<Long> optionAnswersId){
        Map<String, String> params = [userId: surveyRSDTO.user.id.toString(), surveyId: surveyRSDTO.id.toString(),questionId:questionId.toString()]
        Map<String, String> query = [viewerUid:userAnswer.id.toString()]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_ANSWER,
                params,
                query,
                optionAnswersId,
                null
        )
    }

    void sendReport(KuorumUser user, Long surveyId){
        Map<String, String> params = [userId: user.id.toString(), surveyId: surveyId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_REPORT,
                params,
                query,
                null
        )
        response
    }
}
