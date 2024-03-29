<g:if test="${question.contactWeight}">
    <g:if test="${oneOptionQuestion.contains(question.questionType) && question.contactWeight != 1}">
        <div class="survey-question-extra-info question-${question.id}">
            <div class="survey-question-extra-info-range">
                <div class="survey-question-extra-info-points"><g:message
                        code="contact.weighing.question.oneOption" args="[question.contactWeight]"/></div></div>
        </div>
    </g:if>
    <g:if test="${questionTypeMultiples.contains(question.questionType)
            && question.contactWeight != 1
            && question.questionType != org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.ONE_OPTION_WEIGHTED && question.questionType != org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_POINTS}">
        <div class="survey-question-extra-info-points"><g:message
                code="contact.weighing.question.oneOption" args="[question.contactWeight]"/></div>
    </g:if>
    <g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.TEXT_OPTION && question.contactWeight != 1}">
        <div class="survey-question-extra-info question-${question.id}">
            <div class="survey-question-extra-info-range">
                <div class="survey-question-extra-info-points"><g:message
                        code="contact.weighing.question.TEXT_OPTION" args="[question.contactWeight]"/></div>
            </div>
        </div>
    </g:if><g:if
        test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.RATING_OPTION && question.contactWeight != 1}">
    <div class="survey-question-extra-info question-${question.id}">
        <div class="survey-question-extra-info-range">
            <div class="survey-question-extra-info-points"><g:message
                    code="contact.weighing.question.oneOption" args="[question.contactWeight]"/></div>
        </div>
    </div>
</g:if>
</g:if>