<div class="comment-counter pull-right comment-counter-${districtProposal.id}">

    <a type="button" class="${isActive?'active':''} ${callButtonActionClass}"
       href="${callButtonAction}"
       data-campaignValidationActive="${districtProposal.checkValidationActive}"
       data-districtId="${districtProposal.district.id}"
       data-participatoryBudgetId="${districtProposal.participatoryBudget.id}"
       data-proposalId="${districtProposal.id}"
       data-loggedUser="${sec.username()}"
       data-txt-on="${btnStatusTextOn}"
       data-txt-on-hover="${btnStatusTextOnHover}"
       data-txt-off="${btnStatusTextOff}"
       data-txt-off-hover="${btnStatusTextOffHover}">
        <span class="${isActive?'fas':'fal'} ${iconClass}" aria-hidden="true"></span>
        <span class="number">${iconNumber}</span>
        <span class="comment-counter-text">
            <span class="comment-counter-text-active"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.on"/></span>
            <span class="comment-counter-text-active-hover"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.on.hover"/></span>
            <span class="comment-counter-text-inactive"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off"/></span>
            <span class="comment-counter-text-inactive-hover"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off.hover"/></span>
        </span>
    </a>
</div>