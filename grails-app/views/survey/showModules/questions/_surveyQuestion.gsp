<g:set var="questionClass" value="single-answer"/>
<g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.MULTIPLE_OPTION}">
    <g:set var="questionClass" value="multi-answer"/>
</g:if>
<g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.TEXT_OPTION}">
    <g:set var="questionClass" value="text-answer"/>
</g:if>

<li class="comment-box survey-question ${questionClass} no-padding ${survey.closed || question.answered?'answered':''}" data-question-id="${question.id}" data-numAnswers="${question.amountAnswers}" >
    <div class="survery-question-title padding-box">
        ${question.text}
    </div>
    <div class="survey-question-answers" data-answer-selected="">
        <g:each in="${question.options}" var="option">
            <g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.ONE_OPTION}">
                <g:render template="/survey/showModules/questions/singleQuestionOption" model="[survey:survey, question:question, option:option]"/>
            </g:if>
            <g:elseif test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.TEXT_OPTION}">
                <g:render template="/survey/showModules/questions/textQuestionOption" model="[survey:survey, question:question, option:option]"/>
            </g:elseif>
            <g:else>
                <g:render template="/survey/showModules/questions/multipleQuestionOption" model="[survey:survey, question:question, option:option]"/>
            </g:else>
        </g:each>
    </div>
    <div class="footer padding-box">
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign:survey]"/>
        <div class="actions next-section pull-right">
            <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
                <a href="#" target="_blank" class="skip-survey">
                    <g:message code="survey.questions.footer.ownerSurveyNext"/>
                </a>
                <a href="#" target="_blank" class="skip-question">
                    <g:message code="survey.questions.footer.ownerNext"/>
                </a>
            </userUtil:ifUserIsTheLoggedOne>
            <button
                    type="button"
                    class="btn btn-blue btn-lg disabled"
                    data-userLoggedAlias="${userUtil.loggedUserId()}"
                    data-campaignValidationActive="${survey.checkValidationActive}"
                    data-postUrl="${g.createLink(mapping:"surveySaveAnswer", params:survey.encodeAsLinkProperties())}"
                    data-campaignId="${survey.id}"
                    data-campaignAlias="${survey.user.alias}">
                <g:message code="survey.questions.footer.next"/>
            </button>
        </div>
    </div>
</li>