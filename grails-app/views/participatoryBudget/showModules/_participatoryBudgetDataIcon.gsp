<g:if test="${participatoryBudget.published}">
    <div class="comment-counter pull-right">
        <g:link mapping="campaignShow" params="${participatoryBudget.encodeAsLinkProperties()}" fragment="participatory-budget-district-proposals-list-tab" type="button">
            <span class="fal fa-money-bill-alt" aria-hidden="true"></span>
            <span class="number">${participatoryBudget.basicStats.numProposals}</span>
        </g:link>
    </div>
</g:if>
