<div id="question-option-${option.id}"
     class="survey-question-answer ${option.answer!=null?'checked':''} ${option.questionOptionType}"
     data-nextQuestionId="${option.exitSurvey?0:option.nextQuestionId?:''}"
     data-answer-id="${option.id}"
     data-questionOptionType="${option.questionOptionType}"
     data-optionStats-total="${option.optionStats.total}"
     data-optionStats-votes="${option.optionStats.optionVotes}"
     data-optionStats-percentage="${option.optionStats.percentage}">
    <div class="single-option">
        <div class="${isQuestionWithImages?'question-option-image': 'question-option-icon'} ${isQuestionWithImages && !option.imageUrl?'imagen-shadowed-main-color-domain':''}">
            <span class="far fa-circle check-icon"></span>
            <span class="fas fa-check-circle check-icon"></span>
            <if test="${isQuestionWithImages}">
                <img src="${option.imageUrl?:g.resource(dir: "images", file: "emptySquared.png")}"/>
            </if>
        </div>
        <label>${option.text}</label>
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