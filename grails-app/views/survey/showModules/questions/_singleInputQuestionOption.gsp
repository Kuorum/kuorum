<div class="survey-question-answer ${option.answer!=null?'checked':''} ${option.questionOptionType}"
     data-answer-id="${option.id}"
     data-questionOptionType="${option.questionOptionType}"
     data-numAnswers="${option.amountAnswers}">
    <div class="text-option">
        %{--<span class="far fa-circle check-icon"></span>--}%
        %{--<span class="fas fa-check-circle check-icon"></span>--}%
        %{--<label>${option.text}</label>--}%
        <g:render template="/survey/showModules/questions/formQuestionExtraContent" model="[option:option]"/>
    </div>
    <div class="progress-info">
        <div class="progress-bar-counter">${option.amountAnswers}</div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" data-answer-percent-selected="100" data-answer-percent="100">
                <span class="sr-only">100% Complete</span>
            </div>
        </div>
    </div>
</div>