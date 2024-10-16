<%@ page import="org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO; kuorum.web.commands.payment.survey.QuestionLimitAnswersType" %>
<g:if test="${questionTypeMultiples.contains(question.questionType) ||
        question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.ONE_OPTION_WEIGHTED}">
    <div class="survey-question-extra-info question-${question.id}">

        <g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.ONE_OPTION_WEIGHTED}">
            <g:if test="${question.points != 1}">
                <div class="survey-question-extra-info-range"><g:message
                        code="survey.questions.header.extrainfo.multi.QuestionLimitAnswersType.one.option"
                        args="[question.points]"/></div>
            </g:if>
        </g:if>

        <g:else>

            <div class="survey-question-extra-info-range"><g:message
                    code="survey.questions.header.extrainfo.multi.QuestionLimitAnswersType.${kuorum.web.commands.payment.survey.QuestionLimitAnswersType.inferType(question)}"
                    args="[question.minAnswers, question.maxAnswers]"/></div>
            <g:render template="/survey/showModules/questions/surveyQuestionContactWeighing"
                      model="[question: question, oneOptionQuestion: oneOptionQuestion, questionTypeMultiples: questionTypeMultiples]"/>
            <g:if test="${[org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_POINTS].contains(question.questionType)}">
                <div class="survey-question-extra-info-points"><g:message
                        code="survey.questions.header.extrainfo.multi.points" args="[question.points]"/></div>
            </g:if>
            <g:if test="${[org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_WEIGHTED].contains(question.questionType) && question.points <= 1}">
                <div class="survey-question-extra-info-points"></div>
            </g:if>
            <g:if test="${[org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_WEIGHTED].contains(question.questionType) && question.points > 1}">
                <div class="survey-question-extra-info-points"><g:message
                        code="survey.questions.header.extrainfo.multi.points.weigth"
                        args="[(question.maxAnswers ?: question?.options?.size() ?: 0) * question.points, question.minAnswers * question.points, 1, question.points]"/></div>
            </g:if>
        </g:else>
    </div>
</g:if>
<g:else>
    <g:render template="/survey/showModules/questions/surveyQuestionContactWeighing"
              model="[question: question, oneOptionQuestion: oneOptionQuestion, questionTypeMultiples: questionTypeMultiples]"/>
</g:else>
