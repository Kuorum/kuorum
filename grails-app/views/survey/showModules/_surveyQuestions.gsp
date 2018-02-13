<!-- ^survey-progress !-->
<div id="survey-progress" class="comment-box survey-progress" data-question-pos="0">
    <div>Survey progress: <span id="survey-pos">0</span> / <span id="survey-total">4</span></div>
    <div class="progress survey-progress-bar">
        <div id="progress-bar-survey-counter" class="progress-bar" data-amount-percent-per-step="25" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width:0%">
            <span class="sr-only">0% Complete</span>
        </div>
    </div>
</div><!-- ^survey-progress !-->

<g:each in="${survey.questions}" var="question">
    <g:if test="${question.questionType == org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.ONE_OPTION}">
        <g:render template="/survey/showModules/questions/singleQuestion" model="[survey:survey, question:question]"/>
    </g:if>
    <g:else>
        <g:render template="/survey/showModules/questions/multipleQuestion" model="[survey:survey, question: question]"/>
    </g:else>
</g:each>