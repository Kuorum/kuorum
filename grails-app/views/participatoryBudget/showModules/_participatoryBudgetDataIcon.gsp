<g:if test="${participatoryBudget.published}">
    <div class="comment-counter pull-right">
        <button type="button" data-goto="#survey-progress">
            <span class="fal fa-money-bill-alt" aria-hidden="true"></span>
            <span class="number">${participatoryBudget.basicStats.numProposals}</span>
        </button>
    </div>
</g:if>
