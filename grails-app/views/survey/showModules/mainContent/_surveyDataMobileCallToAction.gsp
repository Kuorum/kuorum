<g:set var="callButtonMsg" value="${g.message(code:'survey.callToAction.published.button')}"/>
<g:if test="${survey.published}">
    <g:set var="callButtonMsg" value="${g.message(code:'survey.callToAction.published.button')}"/>
</g:if>
<g:if test="${survey.completed}">
    <g:set var="callButtonMsg" value="${g.message(code:'survey.callToAction.answered.button')}"/>
</g:if>
<g:if test="${survey.closed}">
    <g:if test="${survey.endDate?.before(new Date())}">
        <g:set var="callButtonMsg" value="${g.message(code:"survey.callToAction.closed.button.after")}"/>
    </g:if>
    <g:else>
        <g:set var="callButtonMsg" value="${g.message(code:"survey.callToAction.closed.button.before")}"/>
    </g:else>
</g:if>

<div class="actions call-to-action-mobile go-to-survey">
    %{--EVENT DATA - CHAPU BORRAR --}%
    <button type="button" class="btn btn-blue btn-lg call-message" data-goto="#survey-questions" name="${callButtonMsg}">
        ${callButtonMsg}
    </button>
    <span class="fas fa-caret-down arrow"></span>
    <button type="button" class="btn btn-blue btn-xl btn-circle call-button" data-goto="#survey-questions" name="${callButtonMsg}">
        <span class="fal fa-chart-pie fa-2x"></span>
    </button>
</div>