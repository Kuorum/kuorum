<g:set var="callTitleMsg" value="${g.message(code:'survey.callToAction.draft.title')}"/>
<g:set var="callSubtitleMsg" value="${g.message(code:'survey.callToAction.draft.subtitle')}"/>
<g:set var="callButtonMsg" value=""/>
<g:if test="${survey.published}">
    <g:set var="callTitleMsg" value="${g.message(code:'survey.callToAction.published.title')}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:'survey.callToAction.published.subtitle')}"/>
    <g:set var="callButtonMsg" value="${g.message(code:'survey.callToAction.published.button')}"/>
</g:if>
<g:if test="${survey.completed}">
    <g:set var="callTitleMsg" value="${g.message(code:'survey.callToAction.answered.title')}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:'survey.callToAction.answered.subtitle')}"/>
    <g:set var="callButtonMsg" value="${g.message(code:'survey.callToAction.answered.button')}"/>
</g:if>
<g:if test="${survey.closed}">
    <g:set var="callTitleMsg" value="${g.message(code:'survey.callToAction.closed.title')}"/>
    <g:set var="callClosedTimeAgo"><kuorumDate:humanDate date="${survey.endDate}"/> </g:set>
    <g:set var="callSubtitleMsg" value="${g.message(code:'survey.callToAction.closed.subtitle', args: [callClosedTimeAgo], encodeAs: "raw")}"/>
    <g:set var="callButtonMsg" value="${g.message(code:'survey.callToAction.closed.button')}"/>
</g:if>

<div class="comment-box call-to-action call-to-action-add-proposal hidden-sm hidden-xs">
    <div class="comment-header">
        <span class="call-title">${callTitleMsg}</span>
        <span class="call-subTitle">${callSubtitleMsg}</span>
    </div>
    <g:if test="${survey.published}">
        <div class="actions clearfix">
            <button type="button" class="btn btn-blue btn-lg" data-goto="#survey-progress" id="survey-call-to-action">
                ${callButtonMsg}
            </button>
        </div>
    </g:if>
</div>