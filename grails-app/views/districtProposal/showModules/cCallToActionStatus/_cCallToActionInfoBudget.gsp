<div class="call-district-proposal-info-budget">
    <g:if test="${showPrice}">
        <span class="call-title-price"><g:message code="kuorum.multidomain.money" args="[districtProposal.price.encodeAsReducedPrice()]"/></span>
    </g:if>
    <span class="call-subTitle"><g:message code="districtProposal.callToAction.district.info.budget"/> <g:link mapping="participatoryBudgetShow" params="${districtProposal.participatoryBudget.encodeAsLinkProperties()}" fragment="${districtProposal.district.name.encodeAsKuorumUrl()}">${districtProposal.district.name}</g:link></span>

    <g:if test="${showBudgetProgressBar}">
        <div class="budget">
            <div class="campaign-progress-bar-wrapper">
                <h4><g:message code="districtProposal.callToAction.district.info.progressBar"/>: <span class="budget-price">${districtProposal.district.budget.encodeAsReducedPrice()} €</span></h4>
                <div class="campaign-progress-bar" data-width="${Math.round(districtProposal.district.amountUserInvested / districtProposal.district.budget * 100)}">
                    <div class="pop-up">
                        <g:set var="amountHTML"><span class="amount-user-invested">${districtProposal.district.amountUserInvested}</span></g:set>
                        <g:set var="currency" value="${g.message(code: 'kuorum.multidomain.currency')}"></g:set>
                         <g:message code="districtProposal.callToAction.district.info.progressBar.tooltip" args="[amountHTML, districtProposal.district.budget.encodeAsReducedPrice(), currency]" encodeAs="raw"/>
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
    </g:if>
</div>