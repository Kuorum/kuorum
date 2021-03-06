<div id="question-option-${option.id}"
     class="survey-question-answer survey-question-file-option ${option.answer!=null?'checked':''} ${option.questionOptionType}"
     data-answer-id="${option.id}"
     data-questionOptionType="${option.questionOptionType}"
     data-optionStats-total="${option.optionStats.total}"
     data-optionStats-votes="${option.optionStats.optionVotes}"
     data-optionStats-percentage="${option.optionStats.percentage}">
    <div class="only-predefined-option">
%{--        <span class="far fa-circle check-icon"></span>--}%
%{--        <span class="fas fa-check-circle check-icon"></span>--}%
%{--        <label>${option.text}</label>--}%
        <g:render template="/survey/showModules/questions/formQuestionExtraContent" model="[option:option]"/>
    </div>
    <div class="progress-info">
        <div class="progress-bar-counter">${option.optionStats.optionVotes}</div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" data-answer-percent-selected="50" data-answer-percent="30">
                <span class="sr-only">30% Complete</span>
            </div>
        </div>
    </div>
</div>