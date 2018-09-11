<!-- Modal warn when the district budget is overflow -->
<div class="modal warn-district-overflow" id="warn-district-budget-overflow-${district.id}" tabindex="-1" role="dialog" aria-labelledby="warn-districtProposal-budget-overflow-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>
                <h4 id="warn-districtProposal-budget-overflow-title"><g:message code="participatoryBudget.district.modal.overflow.title" args="[district.name]"/></h4>
            </div>
            <div class="modal-body">
                <p><g:message code="participatoryBudget.district.modal.overflow.text" args="[district.name]"/></p>
                <g:set var="userInvestement" value="${g.message(code:'kuorum.multidomain.money', args: [district.amountUserInvested])}" />
                <g:render template="/participatoryBudget/showModules/mainContent/districtInvestmentProgressBar" model="[
                        district:district,
                        progressBarWidth:Math.round(district.amountUserInvested/district.budget*100),
                        tooltipMsg:g.message(code:'participatoryBudget.progressBar.BALLOT.tooltip', args:[userInvestement, district.name])
                ]"/>
            </div>
            <div class="modal-actions">
                <a href="" role="button" class="btn btn-blue inverted btn-lg close-modal">Lo entiendo</a>
            </div>
        </div>
    </div>
</div>