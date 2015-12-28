<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<formUtil:dynamicComplexInputs
        command="${command}"
        field="politicianRelevantEvents"
        listClassName="kuorum.users.extendedPoliticianData.PoliticianRelevantEvent"
        formId="relevantEventsForm">
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input field="title" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:url field="url" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
    </fieldset>
</formUtil:dynamicComplexInputs>


<fieldset>
    <div class="form-group">
        <div class="col-xs-5 col-xs-offset-1">
            <button type="submit" class="btn btn-default"><g:message code="admin.createUser.submit"/></button>
        </div>
    </div>
</fieldset>