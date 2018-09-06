<div class="call-district-proposal-info-budget">
    <g:if test="${showPrice}">
        <span class="call-title-price"><g:message code="kuorum.multidomain.money" args="[districtProposal.price.encodeAsReducedPrice()]"/></span>
    </g:if>
    <span class="call-subTitle"><g:message code="districtProposal.callToAction.district.info.budget"/> <g:link mapping="participatoryBudgetShow" params="${districtProposal.participatoryBudget.encodeAsLinkProperties()}" fragment="${districtProposal.district.name}">${districtProposal.district.name}</g:link></span>

    <div class="budget">
        <div class="campaign-progress-bar-wrapper">
            <h4><g:message code="districtProposal.callToAction.district.info.progressBar"/>: <span class="budget-price">${districtProposal.district.budget.encodeAsReducedPrice()} €</span></h4>
            <div class="campaign-progress-bar" data-width="${Math.round(districtProposal.district.amountUserInvested / districtProposal.district.budget * 100)}">
                <div class="pop-up">
                    <span class="amount-user-invested">${districtProposal.district.amountUserInvested}</span> gastado de ${districtProposal.district.budget.encodeAsReducedPrice()} €
                    <div class="arrow"></div>
                </div>
                <div class="progress-bar-custom">
                    <div class="progress-bar-custom-done"></div>
                </div>
            </div>
            %{--<div class="campaign-progress-bar-footer">--}%
            %{--<g:link mapping="participatoryBudgetShow" params="${districtProposal.participatoryBudget.encodeAsLinkProperties()}" fragment="${districtProposal.district.name}">--}%
            %{--see to the full budget--}%
            %{--</g:link>--}%
            %{--</div>--}%
        </div>
    </div>
</div>