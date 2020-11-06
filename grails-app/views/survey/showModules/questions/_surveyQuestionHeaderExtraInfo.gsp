<%@ page import="org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO; kuorum.web.commands.payment.survey.QuestionLimitAnswersType" %>
<g:if test="${questionTypeMultiples.contains(question.questionType)}">
    <div class="survey-question-extra-info">
        <div class="survey-question-extra-info-range"><g:message code="survey.questions.header.extrainfo.multi.QuestionLimitAnswersType.${kuorum.web.commands.payment.survey.QuestionLimitAnswersType.inferType(question)}" args="[question.minAnswers, question.maxAnswers]"/></div>
        <g:if test="${[org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_WEIGHTED,org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_POINTS].contains(question.questionType)}">
            <div class="survey-question-extra-info-points"><g:message code="survey.questions.header.extrainfo.multi.points" args="[question.points]"/> </div>
        </g:if>
    </div>
</g:if>