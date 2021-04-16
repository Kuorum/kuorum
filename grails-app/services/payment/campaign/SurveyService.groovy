package payment.campaign


import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.mail.KuorumMailService
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import kuorum.web.commands.payment.survey.SurveyReportType
import org.kuorum.rest.model.communication.survey.*
import org.kuorum.rest.model.communication.survey.answer.QuestionAnswerRDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

@Transactional
class SurveyService extends AbstractCampaignCreatorService<SurveyRSDTO, SurveyRDTO> implements CampaignCreatorService<SurveyRSDTO, SurveyRDTO> {

    def grailsApplication
    def indexSolrService
    def notificationService
    def fileService
    KuorumMailService kuorumMailService
    CampaignService campaignService

    SurveyRSDTO save(KuorumUserSession user, SurveyRDTO surveyRDTO, Long surveyId) {

        SurveyRSDTO survey = null
        if (surveyId) {
            survey = update(user, surveyRDTO, surveyId)
        } else {
            survey = createSurvey(user, surveyRDTO)
        }
        indexSolrService.deltaIndex()
        survey
    }

    private SurveyRSDTO createSurvey(KuorumUserSession user, SurveyRDTO surveyRDTO) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEYS,
                params,
                query,
                surveyRDTO,
                new TypeReference<SurveyRSDTO>() {}
        )

        SurveyRSDTO surveySaved = null
        if (response.data) {
            surveySaved = response.data
        }

        surveySaved
    }

    List<SurveyRSDTO> findAll(KuorumUserSession user, String viewerUid = null) {
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_SURVEYS,
                    params,
                    query,
                    new TypeReference<List<SurveyRSDTO>>() {}
            )

            return response.data ?: null
        } catch (KuorumException e) {
            log.info("Survey not found [Excpt: ${e.message}")
            return null
        }
    }

    SurveyRSDTO find(KuorumUserSession user, Long surveyId, String viewerUid = null) {
        find(user.id.toString(), surveyId, viewerUid)
    }

    SurveyRSDTO find(String userId, Long surveyId, String viewerUid = null) {
        if (!surveyId) {
            return null
        }
        Map<String, String> params = [userId: userId, surveyId: surveyId.toString()]
        Map<String, String> query = [:]
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY,
                    params,
                    query,
                    new TypeReference<SurveyRSDTO>() {}
            )

            return response.data ?: null
        } catch (KuorumException e) {
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
                new TypeReference<SurveyRSDTO>() {}
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
        if (surveyRSDTO) {
            surveyRDTO = campaignService.basicMapping(surveyRSDTO, surveyRDTO)
            surveyRDTO.setVoteType(surveyRSDTO.voteType)
            surveyRDTO.setSignVotes(surveyRSDTO.signVotes)
            //MAP QUESTIONS
            surveyRDTO.questions = surveyRSDTO.questions.collect {
                QuestionRDTO questionRDTO = new QuestionRDTO()
                questionRDTO.id = it.id
                questionRDTO.text = it.text
                questionRDTO.questionType = it.questionType
                questionRDTO.maxAnswers = it.maxAnswers
                questionRDTO.minAnswers = it.minAnswers
                questionRDTO.points = it.points
                questionRDTO.options = it.options.collect { qo ->
                    QuestionOptionRDTO questionOptionRDTO = new QuestionOptionRDTO()
                    questionOptionRDTO.text = qo.text
                    questionOptionRDTO.id = qo.id
                    questionOptionRDTO.questionOptionType = qo.questionOptionType
                    questionOptionRDTO.nextQuestionId = qo.nextQuestionId
                    questionOptionRDTO.exitSurvey = qo.exitSurvey

                    return questionOptionRDTO
                }
                return questionRDTO
            }
        }
        return surveyRDTO
    }

    @Override
    def buildView(SurveyRSDTO campaignRSDTO, BasicDataKuorumUserRSDTO campaignOwner, String viewerUid, def params) {
        Long activeQuestionId = getActiveQuestion(campaignRSDTO)
        def model = [survey: campaignRSDTO, campaignUser: campaignOwner, activeQuestionId: activeQuestionId]
        [view: "/survey/show", model: model]
    }

    private Long getActiveQuestion(SurveyRSDTO surveyRSDTO) {
        if (!surveyRSDTO.questions) {
            //No questions defined (DRAFT SURVEY)
            return 0L
        }
        List<QuestionRSDTO> answeredQuestion = surveyRSDTO.questions.findAll { it.answered }
        if (answeredQuestion.size() == 0) {
            return surveyRSDTO.questions.first().id
        }
        QuestionRSDTO lastQuestionAnswered = answeredQuestion.last();
        QuestionOptionRSDTO optionAnswered = lastQuestionAnswered.options.find { it.answer }
        if (optionAnswered && optionAnswered.nextQuestionId) {
            return optionAnswered.nextQuestionId
        }
        if (optionAnswered && optionAnswered.exitSurvey) {
            return 0L;
        }

        ListIterator<QuestionRSDTO> qit = surveyRSDTO.questions.listIterator();
        QuestionRSDTO nextQuestion = null
        while (nextQuestion == null && qit.hasNext()) {
            QuestionRSDTO q = qit.next();
            if (q.id == lastQuestionAnswered.id && qit.hasNext()) {
                nextQuestion = qit.next();
            }
        }
        if (nextQuestion == null) {
            return 0L;
        } else {
            return nextQuestion.getId();
        }


    }

    QuestionRSDTO saveAnswer(SurveyRSDTO surveyRSDTO, KuorumUserSession userAnswer, Long questionId, List<QuestionAnswerRDTO> answers) {
        Map<String, String> params = [userId: surveyRSDTO.user.id.toString(), surveyId: surveyRSDTO.id.toString(), questionId: questionId.toString()]
        Map<String, String> query = [viewerUid: userAnswer.id.toString()]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_ANSWER,
                params,
                query,
                answers,
                new TypeReference<QuestionRSDTO>() {}
        )
        return response.data
    }

    void sendReport(KuorumUserSession user, Long surveyId, SurveyReportType surveyReportType, Boolean pdfFormat = false) {
        Map<String, String> params = [userId: user.id.toString(), surveyId: surveyId.toString()]
        Map<String, String> query = [pdfFormat: pdfFormat]
        RestKuorumApiService.ApiMethod apiMethod = RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_REPORT_STATS;
        if (SurveyReportType.SURVEY_RAW_DATA.equals(surveyReportType)) {
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

    String uploadQuestionOptionFile(KuorumUserSession user, String aliasUserSurvey, Long surveyId, Long questionId, Long questionOptionId, File file, String fileName) {
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8")
        Map<String, String> params = [userId: aliasUserSurvey, surveyId: surveyId.toString(), questionId: questionId.toString(), questionOptionId: questionOptionId.toString()]
        Map<String, String> query = [viewerUid: user.getId().toString()]
        def response = restKuorumApiService.putFile(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_ANSWER_FILE,
                params,
                query,
                file,
                fileName
        )
    }


    void deleteQuestionOptionFile(KuorumUserSession user, String aliasUserSurvey, Long surveyId, Long questionId, Long questionOptionId, String fileName) {
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8")
        Map<String, String> params = [userId: aliasUserSurvey, surveyId: surveyId.toString(), questionId: questionId.toString(), questionOptionId: questionOptionId.toString()]
        Map<String, String> query = [viewerUid: user.getId().toString(), fileName: fileName]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_ANSWER_FILE,
                params,
                query,
                new TypeReference<String>() {}
        )
    }

    @Override
    protected TypeReference<SurveyRSDTO> getRsdtoType() {
        return new TypeReference<SurveyRSDTO>() {}
    }

    @Override
    protected RestKuorumApiService.ApiMethod getCopyApiMethod() {
        return RestKuorumApiService.ApiMethod.ACCOUNT_SURVEY_COPY;
    }
}
