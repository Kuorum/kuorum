<div class="survey-question-answer ${option.answer!=null?'checked':''}" data-answer-id="${option.id}" data-numAnswers="${option.amountAnswers}">
    <div class="multi-option">
        <span class="fal fa-square check-icon"></span>
        <span class="fas fa-check-square check-icon"></span>
        <label>${option.text}</label>
        <g:if test="${option.questionOptionType == org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO.ANSWER_TEXT}">
            <div class="option-extra-content">
                <textarea>${option.answer?.text?:''}</textarea>
                <span class="text-answer">${option.answer?.text?:''}</span>
            </div>
        </g:if>
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