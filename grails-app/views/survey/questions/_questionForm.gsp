<formUtil:validateForm bean="${command}" form="questionsSurveyForm" dirtyControl="true"/>
<form action="#" class="form-horizontal campaign-form" id="questionsSurveyForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
    <input type="hidden" name="surveyId" value="${command.surveyId}"/>
    <formUtil:dynamicComplexInputs
            command="${command}"
            field="questions"
            listClassName="kuorum.web.commands.payment.survey.QuestionCommand"
            cssParentContainer="profile-dynamic-fields"
            customRemoveButton="true"
            customAddButton="true"
            appendLast="true"
            formId="questionsSurveyForm">
        <fieldset class="row">
            <formUtil:input cssClass="hidden" field="id" command="${listCommand}" prefixFieldName="${prefixField}"/>
            <div class="form-group">
                <label for="text" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.title.label"/>:</label>
                <div class="col-sm-8 col-md-7">
                    <div class="col-md-8 no-padding">
                        <formUtil:input field="text" command="${listCommand}" prefixFieldName="${prefixField}"/>
                    </div>
                    <div class="col-md-3">
                        <formUtil:selectEnum field="questionType" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="false"/>
                    </div>
                    <div class="form-group col-md-1 form-group-remove no-label">
                        <button type="button" class="btn btn-transparent removeButton"><i class="fa fa-trash"></i></button>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="questionOption row">
            <div class="form-group">
                <label for="text" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.title.label"/>:</label>
                <div class="col-sm-8 col-md-7">
                    <g:set var="i" value="${0}"/>
                    <g:each in="${listCommand.options}" var="option">
                        <g:render template="/survey/questions/questionOptionFields" model="[prefixField:prefixField, pos:i, option:option]"/>
                        <g:set var="i" value="${i+1}"/>
                    </g:each>
                    <g:if test="${!listCommand.options}">
                        <g:render template="/survey/questions/questionOptionFields" model="[prefixField:prefixField, pos:i, option:null]"/>
                    </g:if>
                </div>
            </div>
        </fieldset>
    </formUtil:dynamicComplexInputs>
    <fieldset class="row dynamic-fieldset-addbutton">
        <div class="form-group">
            <div class="col-md-12 center">
                <button type="button" class="btn btn-default addButton">Add questions<i class="fa fa-plus"></i></button>
            </div>
        </div>
    </fieldset>
    <g:render template="/campaigns/edit/stepButtons" model="[saveAndSentButtons:saveAndSentButtons, mappings:mappings, status:status, command: command, numberRecipients:numberRecipients]"/>
</form>