<g:if test="${survey.startDate?.after(new Date())}">
    <div class="comment-box survey-warn survey-closed-before">
        <div><g:message code="survey.callToAction.closed.subtitle.before" args="[kuorumDate.humanDate(date:survey.startDate)]" encodeAs="raw"/></div>
    </div>
</g:if>
<g:else>
    <div class="comment-box survey-warn survey-hide-answers-warn">
        <div><g:message code="survey.show.hideResults.warn"/></div>
    </div>
    <!-- ^survey-progress !-->
    <div id="survey-progress-tag" class="comment-box survey-progress" data-question-pos="0">
        <div class="survey-progress-numerical-info">
            <g:message code="survey.show.progress"/>:
            <span id="survey-percentage">0 %</span></span>
%{--            <span id="survey-pos">0</span> / <span id="survey-total">${survey.questions.size()}</span>--}%
        </div>
        <div class="progress survey-progress-bar">
            <div id="progress-bar-survey-counter" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" aria-label="${g.message(code:'survey.show.progress')}" style="width:0%">
                <span class="sr-only">0% Complete</span>
            </div>
        </div>
    </div><!-- ^survey-progress !-->

    <form class="survey-questions ${survey.voteType == org.kuorum.rest.model.communication.survey.SurveyVoteTypeDTO.SECRET?'survey-vote-secret':''}">
        <g:set var="numQuestion" value="${0}"/>
        <g:each in="${survey.questions}" var="question" status="i">
            <g:if test="${question.answered || i==0 || activeQuestionId == question.id}">
                <g:set var="numQuestion" value="${numQuestion+1}"/>
            </g:if>
            <g:render template="/survey/showModules/questions/surveyQuestion" model="[survey:survey, question:question, numQuestion:numQuestion, questionsTotal:survey.questions.size, activeQuestionId:activeQuestionId]"/>
        </g:each>
        <div class="comment-box survey-end ${activeQuestionId == 0?'active-question':''}">
            <div class="survey-end-container">
                <span><g:message code="survey.show.progress.end"/></span>
                <span class="fa fa-laugh-beam"></span>
            </div>
        </div>
    </form>
</g:else>
