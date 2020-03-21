<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[survey.visits]" /></span>
    </li>
    <li>
        <span class="fal fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[campaignUser.numFollowers]" /></span>
    </li>
    <li>
        <span class="fal fa-chart-pie" aria-hidden="true"></span>
        <span class="info"><g:message code="survey.cColumn.stats.tookSuervey" args="[survey.amountAnswers]" /></span>
    </li>
    <g:if test="${survey.startDate}">
        <li>
            <span class="fal fa-calendar-check" aria-hidden="true"></span>
            <span class="info"><g:message code="kuorum.web.commands.payment.SurveyQuestionsCommand.startDate.label"/>: <g:formatDate type="datetime" style="SHORT" date="${survey.startDate}"/></span>
        </li>
    </g:if>
    <g:if test="${survey.endDate}">
        <li>
            <span class="fal fa-calendar-times" aria-hidden="true"></span>
            <span class="info"><g:message code="kuorum.web.commands.payment.SurveyQuestionsCommand.endDate.label"/>: <g:formatDate type="datetime" style="SHORT" date="${survey.endDate}"/></span>
        </li>
    </g:if>
    <g:if test="${survey.hideResults}">
        <li>
            <span class="fal fa-eye-slash" aria-hidden="true"></span>
            <span class="info"><g:message code="survey.cColumn.stats.results.hidden"/></span>
        </li>
    </g:if>
    <g:else>
        <li>
            <span class="fal fa-eye" aria-hidden="true"></span>
            <span class="info"><g:message code="survey.cColumn.stats.results.visible"/></span>
        </li>
    </g:else>
</ul>
