<div class="survey-question-answer ${option.answer!=null?'checked':''}" data-answer-id="${option.id}" data-numAnswers="${option.amountAnswers}">
    <div class="text-option">
        %{--<span class="far fa-circle check-icon"></span>--}%
        %{--<span class="fas fa-check-circle check-icon"></span>--}%
        %{--<label>${option.text}</label>--}%
        %{--<g:if test="${option.questionOptionType == org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO.ANSWER_TEXT}">--}%
            <div class="option-extra-content">
                <textarea>${option.answer?.text?:''}</textarea>
                <span class="text-answer">${option.answer?.text?:''}</span>
            </div>
        %{--</g:if>--}%
    </div>
    <div class="progress-info" style="display: none">
        <div class="progress-bar-counter">${option.amountAnswers}</div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" data-answer-percent-selected="50" data-answer-percent="30">
                <span class="sr-only">30% Complete</span>
            </div>
        </div>
    </div>
</div>