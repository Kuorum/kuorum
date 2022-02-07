<g:set var="hiddenValue" value="${(visibility == null) || visibility ? '' : 'hidden'}"/>
<g:if test="${option.questionOptionType == org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO.ANSWER_TEXT}">
    <div class="form-group option-extra-content">
        <formUtil:textArea
                showLabel="false"
                id="${option.id}_text"
                command="${new kuorum.web.commands.payment.survey.QuestionAnswerDataCommand()}"
                placeholder=""
                field="text"/>
        <span class="text-answer">${option.answer?.text?:''}</span>
    </div>
</g:if>
<g:if test="${option.questionOptionType == org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO.ANSWER_NUMBER}">
    <div class="form-group option-extra-content ${hiddenValue}">
        <formUtil:input
                showLabel="false"
                id="${option.id}_number"
                command="${new kuorum.web.commands.payment.survey.QuestionAnswerDataCommand()}"
                placeholder=""
                numberStep="1"
                field="number"/>
        <span class="text-answer">${option.answer?.number?:''}</span>
    </div>
</g:if>
<g:if test="${option.questionOptionType == org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO.ANSWER_DATE}">
    <div class="form-group option-extra-content">
        <formUtil:date
                datePickerType="birthDate"
                showLabel="false"
                id="${option.id}_date"
                command="${new kuorum.web.commands.payment.survey.QuestionAnswerDataCommand()}"
                placeholder=""
                field="date"/>
        <span class="text-answer"><g:formatDate date="${option.answer?.date}" formatName="default.date.format.small"/></span>
    </div>
</g:if>
<g:if test="${option.questionOptionType == org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO.ANSWER_SMALL_TEXT}">
    <div class="form-group option-extra-content">
        <formUtil:input
                showLabel="false"
                id="${option.id}_text"
                command="${new kuorum.web.commands.payment.survey.QuestionAnswerDataCommand()}"
                placeholder=""
                showCharCounter="false"
                field="text"/>
        <span class="text-answer">${option.answer?.text?:''}</span>
    </div>
</g:if>
<g:if test="${option.questionOptionType == org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO.ANSWER_FILES}">
    <div class="form-group option-extra-content">
        <sec:ifLoggedIn>
            <div class="survey-question-option-extra-info">
                <g:message code="survey.questions.option.ANSWER_FILES.extrainfo"/>
            </div>
            <formUtil:uploadQuestionOptionFiles survey="${survey}" question="${question}" questionOption="${option}"/>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <div class="survey-question-option-extra-info">
                <g:message code="survey.questions.option.ANSWER_FILES.noLogged"/>
                <g:link mapping="login"><g:message code="register.head.login"/></g:link>
            </div>
        </sec:ifNotLoggedIn>
    </div>
</g:if>
<g:if test="${option.questionOptionType == org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO.ANSWER_PHONE}">
    <div class="form-group form-group-phone option-extra-content">
        <formUtil:selectPhonePrefix
                showLabel="false"
                id="${option.id}_text2"
                command="${new kuorum.web.commands.payment.survey.QuestionAnswerDataCommand()}"
                placeholder=""
                required="true"
                field="text2"/>
        <formUtil:input
                showLabel="false"
                id="${option.id}_text"
                command="${new kuorum.web.commands.payment.survey.QuestionAnswerDataCommand()}"
                placeholder=""
                type="number"
                showCharCounter="false"
                field="text"/>

        <span class="text-answer">${option.answer?.prefixPhone?:''}${option.answer?.phone?:''}</span>
    </div>
</g:if>