<g:if test="${contact != null}">
    <g:if test="${oneOptionQuestion.contains(question.questionType) && contact.getSurveyVoteWeight() != 1}">
        <div class="survey-question-extra-info question-${question.id}">
            <div class="survey-question-extra-info-range">
                <div class="survey-question-extra-info-points"><g:message
                        code="contact.weighing.question.oneOption" args="[contact.getSurveyVoteWeight()]"/></div></div>
        </div>
    </g:if>
    <g:if test="${questionTypeMultiples.contains(question.questionType)
            && contact.getSurveyVoteWeight() != 1
            && question.questionType != org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.ONE_OPTION_WEIGHTED}">
        <div class="survey-question-extra-info-points"><g:message
                code="contact.weighing.question.multipleOptions" args="[contact.getSurveyVoteWeight()]"/></div>
    </g:if>
    <g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.TEXT_OPTION}">
        <div class="survey-question-extra-info question-${question.id}">
            <div class="survey-question-extra-info-range">
                <div class="survey-question-extra-info-points"><g:message
                        code="contact.weighing.question.TEXT_OPTION" args="[contact.getSurveyVoteWeight()]"/></div>
            </div>
        </div>
    </g:if>
</g:if>