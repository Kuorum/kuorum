<div class="comment-box">
    <div class="row">
        <div class="label col-xs-12 col-sm-4">
            <g:message code="participatoryBudget.progressBar.label" />
            <g:formatNumber number="${district.budget}" type="currency" maxFractionDigits="0" currencySymbol="${g.message(code:'kuorum.multidomain.currency')}"/>
        </div>
        <div class="campaign-progress-bar-wrapper col-xs-12 col-sm-8">
            <div class="campaign-progress-bar ${importantClass?'important':''}" data-width="${progressBarWidth}">
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