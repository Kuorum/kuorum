<g:set var="questionClass" value="single-answer"/>
<g:set var="questionTypeMultiples" value="${[org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION,
                                             org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_WEIGHTED,
                                             org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION_POINTS]}"/>
<g:if test="${questionTypeMultiples.contains(question.questionType)}">
    <g:set var="questionClass" value="multi-answer"/>
</g:if>
<g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.TEXT_OPTION}">
    <g:set var="questionClass" value="text-answer"/>
</g:if>
<g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.RATING_OPTION}">
    <g:set var="questionClass" value="rating-answer"/>
</g:if>

<li
        class="comment-box survey-question ${questionClass} no-padding ${survey.closed || question.answered?'answered':''} ${activeQuestionId==question.id?'active-question':''}"
        id="question-${question.id}"
        data-question-id="${question.id}"
        data-numAnswers="${question.amountAnswers}"
        data-minAnswers="${question.minAnswers}"
        data-maxAnswers="${question.maxAnswers}"
        data-points="${question.points}"
        data-questionType="${question.questionType}"
>

    <div class="survey-question-header">
        <div class="survery-question-number">
            <span class="survey-quiestion-number-idx">${numQuestion}</span>
%{--            <span class="survey-quiestion-number-total hidden-xs">/${questionsTotal}</span>--}%
            <span class="survey-quiestion-number-total hidden-xs">.</span>
        </div>
        <div class="survey-question-title">
            ${question.text}
        </div>
        <g:render template="/survey/showModules/questions/surveyQuestionHeaderExtraInfo" model="[survey:survey, question:question, questionTypeMultiples:questionTypeMultiples]"/>
    </div>
    <div class="survey-question-answers" data-answer-selected="">
        <g:each in="${question.options}" var="option">
            <g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.ONE_OPTION}">
                <g:render template="/survey/showModules/questions/singleQuestionOption" model="[survey:survey, question:question, option:option]"/>
            </g:if>
            <g:elseif test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.RATING_OPTION}">
                <g:render template="/survey/showModules/questions/ratingQuestionOption" model="[survey:survey, question:question, option:option]"/>
            </g:elseif>
            <g:elseif test="${questionTypeMultiples.contains(question.questionType)}">
                <g:render template="/survey/showModules/questions/multipleQuestionOption" model="[survey:survey, question:question, option:option]"/>
            </g:elseif>
            <g:elseif test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.CONTACT_GENDER}">
                <g:render template="/survey/showModules/questions/genderQuestionOption" model="[survey:survey, question:question, option:option]"/>
            </g:elseif>
            <g:elseif test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.CONTACT_UPLOAD_FILES}">
                <g:render template="/survey/showModules/questions/fileQuestionOption" model="[survey:survey, question:question, option:option]"/>
            </g:elseif>
            <g:else>
                <g:render template="/survey/showModules/questions/singleInputQuestionOption" model="[survey:survey, question:question, option:option]"/>
            </g:else>
        </g:each>
    </div>

    <div class="survey-question-progress-info">
        <div class="progress-info">
            <div class="progress-bar-counter">-</div>
            <div class="progress">
                <div class="progress-bar" role="progressbar" aria-valuenow="3" aria-valuemin="0" aria-valuemax="5" data-answer-percent-selected="50" data-answer-percent="30">
                    <span class="sr-only">30% Complete</span>
                </div>
            </div>
        </div>
    </div>
    <div class="footer padding-box">
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign:survey]"/>
        <div class="actions next-section pull-right">
            <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
                <a href="#" target="_blank" class="skip-survey"><g:message code="survey.questions.footer.ownerSurveyNext"/></a>
                <a href="#" target="_blank" class="skip-question"><g:message code="survey.questions.footer.ownerNext"/></a>
            </userUtil:ifUserIsTheLoggedOne>
            <button
                    type="button"
                    class="btn btn-blue btn-lg disabled"
                    data-userLoggedAlias="${userUtil.loggedUserId()}"
                    data-campaignValidationActive="${survey.checkValidationActive}"
                    data-campaignGroupValidationActive="${survey.groupValidation?g.createLink(mapping: "campaignCheckGroupValidation", params: survey.encodeAsLinkProperties()):''}"
                    data-postUrl="${g.createLink(mapping:"surveySaveAnswer", params:survey.encodeAsLinkProperties())}"
                    data-campaignId="${survey.id}"
                    data-campaignAlias="${survey.user.alias}">
                <g:message code="survey.questions.footer.next"/>
            </button>
        </div>
    </div>
</li>