<formUtil:validateForm bean="${command}" form="questionsSurveyForm" dirtyControl="true"/>
<form action="#"
      class="form-horizontal campaign-form ${status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT || status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PAUSE?'campaign-published':''}"
      id="questionsSurveyForm"
      method="POST"
      data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType" value="${status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT?'SEND':'DRAFT'}" id="sendMassMailingType"/>
    <input type="hidden" name="surveyId" value="${command.surveyId}"/>

    <formUtil:dynamicComplexInputs
            command="${command}"
            field="questions"
            listClassName="kuorum.web.commands.payment.survey.QuestionCommand"
            cssParentContainer="quesiton-dynamic-fields"
            customRemoveButton="true"
            customAddButton="true"
            appendLast="true"
            formId="questionsSurveyForm">
        <fieldset class="row question-data">
            <formUtil:input cssClass="hidden" field="id" command="${listCommand}" prefixFieldName="${prefixField}"/>
            <div class="form-group">
                <label for="text" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.survey.QuestionCommand.text.label"/>:</label>
                <div class="col-xs-12 col-sm-8 col-md-7">
                    <div class="col-xs-12 col-sm-8 no-padding">
                        <formUtil:input field="text" command="${listCommand}" prefixFieldName="${prefixField}"/>
                    </div>
                    <div class="col-xs-11 col-sm-3 question-type">
                        <span class="question-type-noEditable">${message(code:"org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.${listCommand.questionType}")}</span>
                        <formUtil:selectEnum field="questionType" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="false"/>
                    </div>
                    <div class="col-xs-1 no-label-lg">
                        <button type="button" class="btn btn-transparent btn-lg btn-icon removeButton"><i class="fal fa-trash"></i></button>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="row question-options">
            <div class="form-group">
                <label for="text" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.survey.QuestionCommand.options.label"/>:</label>
                <div class="col-sm-8 col-md-7 questionOption">
                    <g:set var="i" value="${0}"/>
                    <g:each in="${listCommand.options}" var="option">
                        <g:render template="/survey/questions/questionOptionFields" model="[prefixField:prefixField, pos:i, option:option]"/>
                        <g:set var="i" value="${i+1}"/>
                    </g:each>
                    <g:if test="${!listCommand.options}">
                        <g:render template="/survey/questions/questionOptionFields" model="[prefixField:prefixField, pos:i, option:null]"/>
                    </g:if>
                </div>
                <div class="col-sm-8 col-md-7 questionOptionActions">
                    <fieldset class="row">
                        <div class="col-xs-offset-1 col-xs-11">
                            <button type="button" class="btn btn-lg btn-icon btn-transparent addQuestionOptionButton">
                                <g:message code="survey.form.button.addOption"/>
                                <i class="far fa-plus"></i>
                            </button>
                        </div>
                    </fieldset>
                </div>
            </div>
        </fieldset>
    </formUtil:dynamicComplexInputs>
    <fieldset class="row dynamic-fieldset-addbutton">
        <div class="form-group">
            <div class="col-xs-12 center">
                <button type="button" class="btn btn-lg btn-grey addButton">
                    <g:message code="survey.form.button.addQuestion"/>
                    <i class="far fa-plus"></i>
                </button>
                <button type="button" class="btn btn-lg btn-grey inverted reorderQuestionsButton">
                    <g:message code="survey.form.button.reorderQuestions.start"/>
                    <i class="far fa-sort-alt"></i>
                </button>
                <button type="button" class="btn btn-lg btn-blue endReorderQuestionsButton">
                    <g:message code="survey.form.button.reorderQuestions.end"/>
                    <i class="far fa-sort-alt"></i>
                </button>
            </div>
        </div>
    </fieldset>
    <g:render template="/campaigns/edit/stepButtons" model="[
            saveAndSentButtons:true,
            mappings:mappings,
            status:status,
            command: command,
            numberRecipients:numberRecipients]"/>
</form>