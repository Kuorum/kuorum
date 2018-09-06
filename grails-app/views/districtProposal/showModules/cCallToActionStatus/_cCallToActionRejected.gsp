<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.TECHNICAL_REVIEW.NO_VALID.title", args: [campaignUser.name])}"/>
<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.TECHNICAL_REVIEW.NO_VALID.subtitle", args: [campaignUser.name])}"/>
<g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionHeader" model="[callTitleMsg:callTitleMsg, callSubtitleMsg:callSubtitleMsg]"/>
<hr/>
<div class="call-district-proposal-info-budget rejected">
    <span class="call-title">Motivo</span>
    <span class="call-subTitle">"${districtProposal.rejectComment}"</span>
</div>