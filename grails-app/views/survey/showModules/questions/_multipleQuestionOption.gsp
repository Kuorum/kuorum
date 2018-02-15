<div class="survey-question-answer ${option.checked?'checked':''}" data-answer-id="${option.id}" data-numAnswers="${option.amountAnswers}">
    <div class="multi-option">
        <span class="fa fa-square-o check-icon"></span>
        <span class="fa fa-check-square check-icon"></span>
        <span>${option.text}</span>
    </div>
    <div class="progress-info">
        <div class="progress-bar-counter">${option.amountAnswers}</div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" data-answer-percent-selected="50" data-answer-percent="49">
                <span class="sr-only">49 votes</span>
            </div>
        </div>
    </div>
</div>