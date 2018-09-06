<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.title", args: [campaignUser.name])}"/>
<g:set var="dateEndTime"><g:formatDate date="${districtProposal.participatoryBudget.deadLineTechnicalReview}" type="date" style="short"/></g:set>
<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.subtitle", args: [dateEndTime])}"/>
<g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionHeader" model="[callTitleMsg:callTitleMsg, callSubtitleMsg:callSubtitleMsg]"/>
<hr/>
<g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionInfoBudget" model="[districtProposal:districtProposal, showPrice:false]"/>
