<div class="survey-question-answer survey-question-rating-option ${option.answer!=null?'checked':''}" data-answer-id="${option.id}" data-numAnswers="${option.amountAnswers}">
    <div class="rating-option">
        <span class="fal fa-square check-icon"></span>
        <span class="fas fa-square check-icon"></span>
        <input type="radio" name="rating" value="${option.id}">
        <label>${option.text}</label>
        %{--<g:render template="/survey/showModules/questions/formQuestionExtraContent" model="[option:option]"/>--}%
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