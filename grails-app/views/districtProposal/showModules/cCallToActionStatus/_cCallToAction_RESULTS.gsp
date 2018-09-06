<g:set var="callButtonAction" value="${g.createLink(mapping:'participatoryBudgetDistrictProposalSupport', params:districtProposal.encodeAsLinkProperties())}"/>
<g:set var="callButtonActive" value="${districtProposal.isSupported}"/>
<g:set var="btnStatusTextOn"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.on"/></g:set>
<g:set var="btnStatusTextOnHover"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.on.hover"/></g:set>
<g:set var="btnStatusTextOff"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off"/></g:set>
<g:set var="btnStatusTextOffHover"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off.hover"/></g:set>

<g:if test="${districtProposal.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.VALID}">
    <g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.subtitle", args: [campaignUser.name])}"/>
    <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionHeader" model="[callTitleMsg:callTitleMsg, callSubtitleMsg:callSubtitleMsg]"/>
    <hr/>
    <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionInfoBudget" model="[districtProposal:districtProposal, showPrice:true]"/>
</g:if>
<g:else>
    <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionRejected" model="[districtProposal:districtProposal, campaignUser:campaignUser]"/>
</g:else>