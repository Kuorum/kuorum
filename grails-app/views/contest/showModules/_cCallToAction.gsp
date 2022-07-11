<g:set var="callTitleMsg" value="${g.message(code: 'contest.callToAction.draft.title')}"/>
<g:set var="callSubtitleMsg" value="${g.message(code: 'contest.callToAction.draft.subtitle')}"/>
<g:set var="callButtonMsg" value=""/>
<g:if test="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED.equals(contest.newsletter?.status ?: null)}">
    <g:set var="callTitleMsg"
           value="${g.message(code: "contest.callToAction.SCHEDULED.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg"
           value="${g.message(code: "contest.callToAction.SCHEDULED.subtitle", args: [campaignUser.name])}"/>
</g:if>
<g:elseif
        test="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PROCESSING.equals(contest.newsletter?.status ?: null)}">
    <g:set var="callTitleMsg"
           value="${g.message(code: "contest.callToAction.PROCESSING.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg"
           value="${g.message(code: "contest.callToAction.PROCESSING.subtitle", args: [campaignUser.name])}"/>
</g:elseif>
<g:elseif test="${contest.published}">
    <g:set var="callTitleMsg"
           value="${g.message(code: "contest.callToAction.${contest.status}.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg"
           value="${g.message(code: "contest.callToAction.${contest.status}.subtitle", args: [campaignUser.name])}"/>
    <g:set var="callButtonMsg"
           value="${g.message(code: "contest.callToAction.${contest.status}.button", args: [campaignUser.name])}"/>
</g:elseif>

<div class="comment-box call-to-action ${hideXs ? 'hidden-sm hidden-xs' : ''}">
    <div class="comment-header">
        <span class="call-title">${callTitleMsg}</span>
        <span class="call-subTitle">${callSubtitleMsg}</span>
    </div>
    <g:if test="${contest.published}">
        <div class="actions clearfix">
            <g:if test="${contest.status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.ADDING_APPLICATIONS}">
                <g:link
                        mapping="contestApplicationCreate"
                        params="${contest.encodeAsLinkProperties()}"
                        type="button"
                        data-loggedUser="${sec.username()}"
                        class="btn btn-blue btn-lg ${contest.status}">
                    ${callButtonMsg}
                </g:link>
            </g:if>
            <g:elseif
                    test="${contest.status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.VALIDATING_APPLICATIONS}">
            </g:elseif>
            <g:elseif test="${contest.status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING}">
                <a href="#contest-applications-list" class="btn btn-blue btn-lg ${contest.status}">${callButtonMsg}</a>
            </g:elseif>
            <g:elseif test="${contest.status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS}">
                <a href="#contest-applications-list" class="btn btn-blue btn-lg ${contest.status}">${callButtonMsg}</a>
            </g:elseif>
        </div>
    </g:if>
</div>