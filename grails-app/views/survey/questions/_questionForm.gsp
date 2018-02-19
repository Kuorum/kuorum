<formUtil:validateForm bean="${command}" form="questionsSurveyForm" dirtyControl="true"/>
<form action="#" class="form-horizontal campaign-form" id="questionsSurveyForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
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
            <div class="form-group col-md-9 question-text-container">
                <formUtil:input field="text" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
            </div>
            <div class="form-group col-md-3">
                <formUtil:selectEnum field="questionType" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
            </div>
            <div class="form-group col-md-1 form-group-remove">
                <button type="button" class="btn btn-transparent removeButton"><i class="fa fa-trash"></i></button>
            </div>
        </fieldset>

        <fieldset class="questionOption row">
            <g:set var="i" value="${0}"/>
            <g:each in="${listCommand.options}" var="option">
                <g:render template="/survey/questions/questionOptionFields" model="[prefixField:prefixField, pos:i, option:option]"/>
                <g:set var="i" value="${i+1}"/>
            </g:each>
            <g:if test="${!listCommand.options}">
                <g:render template="/survey/questions/questionOptionFields" model="[prefixField:prefixField, pos:i, option:null]"/>
            </g:if>
        </fieldset>
    </formUtil:dynamicComplexInputs>
    <fieldset class="row dynamic-fieldset-addbutton">
        <div class="form-group">
            <div class="col-md-12 center">
                <button type="button" class="btn btn-default addButton">Add questions<i class="fa fa-plus"></i></button>
            </div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <div class="text-right">
            <ul class="form-final-options">
                <li>
                    <a href="#" id="save-draft-debate" data-redirectLink="politicianCampaigns">
                        <g:message code="tools.massMailing.saveDraft"/>
                    </a>
                </li>
                <li><a href="#" class="btn btn-blue inverted" id="next" data-redirectLink="${mappings.next}"><g:message code="tools.massMailing.next"/></a></li>
            </ul>
        </div>
    </fieldset>
</form>