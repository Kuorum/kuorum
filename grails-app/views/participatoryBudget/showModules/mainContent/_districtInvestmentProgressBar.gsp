<div class="comment-box">
    <div class="row">
        <div class="label col-xs-12 col-sm-4">
            <g:if test="${district.basicParticipatoryBudget.participatoryBudgetType == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetTypeDTO.SIMPLE_VOTE}">
                <g:message code="participatoryBudget.progressBar.label.singleVote"/>:
                <g:formatNumber number="${district.budget}" type="number" maxFractionDigits="0"/>
            </g:if>
            <g:else>
                <g:message code="participatoryBudget.progressBar.label"/>
                <g:formatNumber number="${district.budget}" type="currency" maxFractionDigits="0"
                                currencySymbol="${g.message(code: 'kuorum.multidomain.currency')}"/>
            </g:else>
        </div>

        <div class="campaign-progress-bar-wrapper col-xs-12 col-sm-8">
            <div class="campaign-progress-bar ${importantClass ? 'important' : ''}" data-width="${progressBarWidth}">
                <div class="pop-up">
                    ${raw(tooltipMsg)}
                    <div class="arrow"></div>
                </div>

                <div class="progress-bar-custom">
                    <div class="progress-bar-custom-done"></div>
                </div>
            </div>
        </div>
    </div>
</div>