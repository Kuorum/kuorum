<div class="comment-counter pull-right" id="comment-counter-${districtProposal.id}">

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
    </a>
</div>