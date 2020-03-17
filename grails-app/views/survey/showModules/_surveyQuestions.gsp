<g:if test="${survey.published}">
    <!-- ^survey-progress !-->
    <div id="survey-progress-tag" class="comment-box survey-progress" data-question-pos="0">
        <div><g:message code="survey.show.progress"/>: <span id="survey-pos">0</span> / <span id="survey-total">${survey.questions.size()}</span></div>
        <div class="progress survey-progress-bar">
            <div id="progress-bar-survey-counter" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width:0%">
                <span class="sr-only">0% Complete</span>
            </div>
        </div>
    </div><!-- ^survey-progress !-->

    <ul class="survey-questions ${survey.hideResults?'campaign-hide-results':''}">
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
</g:if>