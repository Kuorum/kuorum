<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.draft.title", args: [campaignUser.name])}"/>
<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.draft.subtitle")}"/>
<g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionHeader" model="[callTitleMsg:callTitleMsg, callSubtitleMsg:callSubtitleMsg]"/>
<hr/>
<g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionInfoBudget" model="[districtProposal:districtProposal, showPrice:false, showBudgetProgressBar:false]"/>