<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<formUtil:dynamicComplexInputs
        command="${command}"
        field="timeLine"
        listClassName="kuorum.users.extendedPoliticianData.PoliticianTimeLine"
        formId="politicianExperienceForm">
    <fieldset class="row">
        <div class="form-group col-md-12">
            <formUtil:input field="title" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-12">
            <formUtil:textArea field="text" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:date field="date" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:checkBox field="important" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
    </fieldset>
</formUtil:dynamicComplexInputs>


<fieldset>
    <div class="form-group">
        <div class="col-xs-5 col-xs-offset-1">
            <button type="submit" class="btn btn-default"><g:message code="admin.createUser.submit"/> </button>
        </div>
    </div>
</fieldset>
