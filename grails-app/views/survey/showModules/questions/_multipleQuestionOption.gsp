<g:set var="hiddenValue" value="${question.points <= 1?'hidden':''}"/>
<div id="question-option-${option.id}"
     class="survey-question-answer ${option.answer!=null?'checked':''} ${option.questionOptionType}"
     data-answer-id="${option.id}"
     data-optionStats-total="0"
     data-optionStats-votes="0"
     data-optionStats-percentage="0"
     data-questionOptionType="${option.questionOptionType}">
    <div class="multi-option">
        <g:render template="/survey/showModules/questions/optionIcon"
                  model="[
                          option: option,
                          isQuestionWithImages:isQuestionWithImages,
                          faClassEmpty:'fal fa-square',
                          faClassChecked:'fas fa-check-square']"/>
        <label class="${isQuestionWithImages?'col-xs-12 col-sm-9': 'col-xs-10'}">${option.text}</label>
        <g:render template="/survey/showModules/questions/formQuestionExtraContent" model="[option:option, visibility:hiddenValue]"/>

    </div>
    <div class="progress-info">
        <div class="progress-bar-counter"><span class="fas fa-spinner fa-spin"></span></div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" data-answer-percent-selected="50" data-answer-percent="49">
                <span class="sr-only">49 votes</span>
            </div>
        </div>
    </div>
</div>