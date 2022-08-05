<g:set var="enabledForAdmin" value="${grails.plugin.springsecurity.SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN")}"/>
<g:set var="enabledForSuperAdmin" value="${grails.plugin.springsecurity.SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN")}"/>
<g:set var="callTitleMsg" value="${g.message(code: 'survey.callToAction.draft.title')}"/>
<g:set var="callSubtitleMsg" value="${g.message(code: 'survey.callToAction.draft.subtitle')}"/>
<g:set var="callButtonMsg" value=""/>
<g:if test="${survey.published}">
    <g:set var="callTitleMsg" value="${g.message(code: 'survey.callToAction.published.title')}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code: 'survey.callToAction.published.subtitle')}"/>
    <g:set var="callButtonMsg" value="${g.message(code: 'survey.callToAction.published.button')}"/>
</g:if>
<g:if test="${survey.completed}">
    <g:set var="callTitleMsg" value="${g.message(code: 'survey.callToAction.answered.title')}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code: 'survey.callToAction.answered.subtitle')}"/>
    <g:set var="callButtonMsg" value="${g.message(code: 'survey.callToAction.answered.button')}"/>
</g:if>
<g:if test="${survey.closed}">
    <g:set var="callTitleMsg" value="${g.message(code: 'survey.callToAction.closed.title')}"/>
    <g:if test="${survey.endDate?.before(new Date())}">
        <g:set var="callClosedTimeAgo"><kuorumDate:humanDate date="${survey.endDate}"/></g:set>
        <g:set var="callSubtitleMsg"
        value="${g.message(code: "survey.callToAction.closed.subtitle.after", args: [callClosedTimeAgo], encodeAs: "raw")}"/>
        <g:set var="callButtonMsg" value="${g.message(code: "survey.callToAction.closed.button.after")}"/>
    </g:if>
    <g:else>
        <g:set var="callClosedTimeAgo"><kuorumDate:humanDate date="${survey.startDate}"/></g:set>
        <g:set var="callSubtitleMsg"
        value="${g.message(code: "survey.callToAction.closed.subtitle.before", args: [callClosedTimeAgo], encodeAs: "raw")}"/>
        <g:set var="callButtonMsg" value="${g.message(code: "survey.callToAction.closed.button.before")}"/>
    </g:else>
</g:if>

<div class="comment-box call-to-action call-to-action-add-proposal hidden-sm hidden-xs">
    <div class="comment-header">
        <span class="call-title">${callTitleMsg}</span>
        <span class="call-subTitle">${callSubtitleMsg}</span>
    </div>
    <g:if test="${ survey.published && survey.campaignVisibility == org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO.VISIBLE ||
            enabledForAdmin && survey.campaignVisibility == org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO.RESTRICTED || enabledForSuperAdmin}">
        <div class="actions clearfix">
            <button type="button" class="btn btn-blue btn-lg" data-goto="#survey-questions"
                    id="survey-call-to-action">
                ${callButtonMsg}
            </button>
        </div>
    </g:if>
</div>