<div class="comment-box">
    <div class="row">
        <div class="col-md-3 label">
            <g:message code="participatoryBudget.progressBar.label" />
            <g:message code="kuorum.multidomain.money" args="[district.budget.encodeAsReducedPrice()]"/>
        </div>
        <div class="col-md-9 campaign-progress-bar-wrapper">
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