<g:if test="${districtProposal.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.VALID}">
    <g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.RESULTS.${districtProposal.implemented?'implemented':'notImplemented'}.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.RESULTS.${districtProposal.implemented?'implemented':'notImplemented'}.subtitle", args: [campaignUser.name])}"/>
    <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionHeader" model="[callTitleMsg:callTitleMsg, callSubtitleMsg:callSubtitleMsg]"/>
    <hr/>
    <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionInfoBudget"
              model="[districtProposal: districtProposal, showPrice: districtProposal.participatoryBudget.participatoryBudgetType == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetTypeDTO.BUDGET, showBudgetProgressBar: false]"/>
</g:if>
<g:else>
    <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionRejected" model="[districtProposal:districtProposal, campaignUser:campaignUser]"/>
</g:else>