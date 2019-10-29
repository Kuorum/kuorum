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