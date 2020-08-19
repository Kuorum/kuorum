<%@ page import="kuorum.web.commands.payment.survey.QuestionLimitAnswersType" %>
<g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION}">
    <div class="survey-question-extra-info">
        <g:message code="survey.questions.header.extrainfo.multi.QuestionLimitAnswersType.${kuorum.web.commands.payment.survey.QuestionLimitAnswersType.inferType(question)}" args="[question.minAnswers, question.maxAnswers]"/>
    </div>
</g:if>