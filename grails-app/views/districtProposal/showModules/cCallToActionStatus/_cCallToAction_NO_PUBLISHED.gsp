<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.draft.title", args: [campaignUser.name])}"/>
<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.draft.subtitle")}"/>
<g:if test="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED.equals(districtProposal.newsletter?.status?:null)}">
    <g:set var="callTitleMsg" value="${g.message(code:"participatoryBudget.callToAction.SCHEDULED.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"participatoryBudget.callToAction.SCHEDULED.subtitle", args: [campaignUser.name])}"/>
</g:if>
<g:elseif test="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PROCESSING.equals(districtProposal.newsletter?.status?:null)}">
    <g:set var="callTitleMsg" value="${g.message(code:"participatoryBudget.callToAction.PROCESSING.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"participatoryBudget.callToAction.PROCESSING.subtitle", args: [campaignUser.name])}"/>
</g:elseif>
<g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionHeader" model="[callTitleMsg:callTitleMsg, callSubtitleMsg:callSubtitleMsg]"/>
<hr/>
<g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionInfoBudget" model="[districtProposal:districtProposal, showPrice:false, showBudgetProgressBar:false]"/>