<div class="comment-counter pull-right">
    <g:if test="${participatoryBudget.status==org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS}">
        <r:require modules="participatoryBudget"/>
        <g:link
                mapping="districtProposalCreate" params="${participatoryBudget.encodeAsLinkProperties()}"
                class="${participatoryBudget.status}"
                data-loggedUser="${sec.username()}"
                type="button">
            <span class="fal fa-money-bill-alt" aria-hidden="true"></span>
            <span class="number">${participatoryBudget.basicStats.numProposals}</span>
        </g:link>
    </g:if>
    <g:else>
        <g:link mapping="campaignShow" params="${participatoryBudget.encodeAsLinkProperties()}" fragment="participatory-budget-district-proposals-list-tab" type="button">
            <span class="fal fa-money-bill-alt" aria-hidden="true"></span>
            <span class="number">${participatoryBudget.basicStats.numProposals}</span>
        </g:link>
    </g:else>
</div>
