<div class="survey-question-answer ${option.checked?'checked':''}" data-answer-id="${option.id}" data-numAnswers="${option.amountAnswers}">
    <div class="option">
        <span class="fa fa-circle-thin check-icon"></span>
        <span class="fa fa-check-circle check-icon"></span>
        <span>${option.text}</span>
    </div>
    <div class="progress-info">
        <div class="progress-bar-counter">${option.amountAnswers}</div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" data-answer-percent-selected="50" data-answer-percent="30">
                <span class="sr-only">30% Complete</span>
            </div>
        </div>
    </div>
</div>