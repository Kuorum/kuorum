<div
        class="survey-question-answer ${option.answer!=null?'checked':''} ${option.questionOptionType}"
        data-nextQuestionId="${option.exitSurvey?0:option.nextQuestionId?:''}"
        data-answer-id="${option.id}"
        data-questionOptionType="${option.questionOptionType}"
        data-numAnswers="${option.amountAnswers}">
    <div class="single-option">
        <span class="far fa-circle check-icon"></span>
        <span class="fas fa-check-circle check-icon"></span>
        <label><g:message code="org.kuorum.rest.model.contact.GenderRDTO.${option.text}"/></label>
        <g:render template="/survey/showModules/questions/formQuestionExtraContent" model="[option:option]"/>
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