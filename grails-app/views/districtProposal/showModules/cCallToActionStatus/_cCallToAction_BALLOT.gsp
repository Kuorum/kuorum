<g:set var="callButtonAction" value="${g.createLink(mapping:'participatoryBudgetDistrictProposalVote', params:districtProposal.encodeAsLinkProperties())}"/>
<g:set var="callButtonActive" value="${districtProposal.isVoted}"/>
<g:set var="btnStatusTextOn"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.on"/></g:set>
<g:set var="btnStatusTextOnHover"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.on.hover"/></g:set>
<g:set var="btnStatusTextOff"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off"/></g:set>
<g:set var="btnStatusTextOffHover"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off.hover"/></g:set>

<g:if test="${districtProposal.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.VALID}">
    <g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.title", args: [campaignUser.name])}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.subtitle", args: [campaignUser.name])}"/>
    <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionHeader" model="[callTitleMsg:callTitleMsg, callSubtitleMsg:callSubtitleMsg]"/>
    <div class="actions clearfix">
        <a
                href="${callButtonAction}"
                type="button"
                class="btn btn-blue-status btn-lg ${callButtonActive?'on':''} districtProposal-vote"
                data-campaignGroupValidationActive="${districtProposal.groupValidation?g.createLink(mapping: "campaignCheckGroupValidation", params: districtProposal.encodeAsLinkProperties()):''}"
                data-campaignValidationActive="${districtProposal.checkValidationActive}"
                data-districtId="${districtProposal.district.id}"
                data-campaignId="${districtProposal.id}"
                data-participatoryBudgetId="${districtProposal.participatoryBudget.id}"
                data-proposalId="${districtProposal.id}"
                data-loggedUser="${sec.username()}"
                data-txt-on="${btnStatusTextOn}"
                data-txt-on-hover="${btnStatusTextOnHover}"
                data-txt-off="${btnStatusTextOff}"
                data-txt-off-hover="${btnStatusTextOffHover}"
        >
            <span class="fal fa-shopping-cart"></span>
        </a>
    </div>
    <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionInfoBudget"
              model="[districtProposal: districtProposal, showPrice: districtProposal.participatoryBudget.participatoryBudgetType == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetTypeDTO.BUDGET, showBudgetProgressBar: true]"/>
    <!-- District modal overflow -->
    <g:render template="/districtProposal/showModules/mainContent/districtProposalModalErrors" model="[district:districtProposal.district]"/>
</g:if>
<g:else>
   <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToActionRejected" model="[districtProposal:districtProposal, campaignUser:campaignUser]"/>
</g:else>