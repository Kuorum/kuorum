<g:set var="labelOptionId" value="label-option-${option.id}"/>
<div
        id="question-option-${option.id}"
        class="survey-question-answer ${option.answer!=null?'checked':''} ${option.questionOptionType}"
        data-nextQuestionId="${option.exitSurvey?0:option.nextQuestionId?:''}"
        data-answer-id="${option.id}"
        data-questionOptionType="${option.questionOptionType}"
        data-optionStats-total="0"
        data-optionStats-votes="0"
        data-optionStats-percentage="0">
    <div class="single-option">
        <g:render template="/survey/showModules/questions/optionIcon"
                  model="[
                          option              : option,
                          optionIdx           : optionIdx,
                          roleInput           : 'radio',
                          labelOptionId       : labelOptionId,
                          isQuestionWithImages: false,
                          faClassEmpty        : 'far fa-circle',
                          faClassChecked      : 'fas fa-check-circle']"/>
        <label class="col-xs-10" id="${labelOptionId}"><g:message code="org.kuorum.rest.model.contact.GenderRDTO.${option.text}"/></label>
        <g:render template="/survey/showModules/questions/formQuestionExtraContent" model="[option:option, visibility: true]"/>
    </div>
    <div class="progress-info">
        <div class="progress-bar-counter"><span class="fas fa-spinner fa-spin"></span></div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" data-answer-percent-selected="50" data-answer-percent="30">
                <span class="sr-only">30% Complete</span>
            </div>
        </div>
    </div>
</div>