<div id="question-option-${option.id}"
     class="survey-question-answer ${option.answer!=null?'checked':''} ${option.questionOptionType}"
     data-answer-id="${option.id}"
     data-optionStats-total="${option.optionStats.total}"
     data-optionStats-votes="${option.optionStats.optionVotes}"
     data-optionStats-percentage="${option.optionStats.percentage}"
     data-questionOptionType="${option.questionOptionType}">
    <div class="multi-option">
        <g:render template="/survey/showModules/questions/optionIcon"
                  model="[
                          option: option,
                          isQuestionWithImages:isQuestionWithImages,
                          faClassEmpty:'fal fa-square',
                          faClassChecked:'fas fa-check-square']"/>
        <label class="${isQuestionWithImages?'col-xs-12 col-sm-7': 'col-xs-10'}">${option.text}</label>
        <g:render template="/survey/showModules/questions/formQuestionExtraContent" model="[option:option]"/>
    </div>
    <div class="progress-info">
        <div class="progress-bar-counter">${option.optionStats.optionVotes}</div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" data-answer-percent-selected="50" data-answer-percent="49">
                <span class="sr-only">49 votes</span>
            </div>
        </div>
    </div>
</div>