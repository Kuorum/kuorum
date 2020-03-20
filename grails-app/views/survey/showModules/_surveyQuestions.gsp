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
        <div><g:message code="survey.show.progress"/>: <span id="survey-pos">0</span> / <span id="survey-total">${survey.questions.size()}</span></div>
        <div class="progress survey-progress-bar">
            <div id="progress-bar-survey-counter" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width:0%">
                <span class="sr-only">0% Complete</span>
            </div>
        </div>
    </div><!-- ^survey-progress !-->

    <ul id="survey-questions-tag" class="survey-questions">
        <g:each in="${survey.questions}" var="question" status="i">
            <g:render template="/survey/showModules/questions/surveyQuestion" model="[survey:survey, question:question, questionNumber:i+1, questionsTotal:survey.questions.size]"/>
        </g:each>
        <li class="comment-box survey-end">
            <div class="survey-end-container">
                <span><g:message code="survey.show.progress.end"/></span>
                <span class="fa fa-laugh-beam"></span>
            </div>
        </li>
    </ul>
</g:else>
