<g:set var="callTitleMsg" value="${g.message(code:'participatoryBudget.callToAction.draft.title')}"/>
<g:set var="callSubtitleMsg" value="${g.message(code:'participatoryBudget.callToAction.draft.subtitle')}"/>
<g:set var="callButtonMsg" value=""/>
<g:if test="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED.equals(participatoryBudget.newsletter?.status?:null)}">
    <g:set var="callTitleMsg" value="${g.message(code:"participatoryBudget.callToAction.SCHEDULED.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"participatoryBudget.callToAction.SCHEDULED.subtitle", args: [campaignUser.name])}"/>
</g:if>
<g:elseif test="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PROCESSING.equals(participatoryBudget.newsletter?.status?:null)}">
    <g:set var="callTitleMsg" value="${g.message(code:"participatoryBudget.callToAction.PROCESSING.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"participatoryBudget.callToAction.PROCESSING.subtitle", args: [campaignUser.name])}"/>
</g:elseif>
<g:elseif test="${participatoryBudget.published}">
    <g:set var="callTitleMsg" value="${g.message(code:"participatoryBudget.callToAction.${participatoryBudget.status}.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"participatoryBudget.callToAction.${participatoryBudget.status}.subtitle", args: [campaignUser.name])}"/>
    <g:set var="callButtonMsg" value="${g.message(code:"participatoryBudget.callToAction.${participatoryBudget.status}.button",args: [campaignUser.name])}"/>
</g:elseif>

<div class="comment-box call-to-action call-to-action-add-proposal ${hideXs?'hidden-sm hidden-xs':''}">
    <div class="comment-header">
        <span class="call-title">${callTitleMsg}</span>
        <span class="call-subTitle">${callSubtitleMsg}</span>
    </div>
    <g:if test="${participatoryBudget.published}">
        <div class="actions clearfix">
            <g:link
                    mapping="districtProposalCreate"
                    params="${participatoryBudget.encodeAsLinkProperties()}"
                    type="button"
                    data-loggedUser="${sec.username()}"
                    class="btn btn-blue btn-lg ${participatoryBudget.status}">
                ${callButtonMsg}
            </g:link>
        </div>
    </g:if>
</div>