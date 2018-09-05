<r:require modules="forms"/>
<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<formUtil:dynamicComplexInputs
        command="${command}"
        field="politicianRelevantEvents"
        listClassName="kuorum.users.extendedPoliticianData.PoliticianRelevantEvent"
        cssParentContainer="profile-dynamic-fields"
        customRemoveButton="true"
        formId="relevantEventsForm">
    <fieldset class="row">
        <div class="form-group col-md-5">
            <formUtil:input field="title" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-5">
            <formUtil:url field="url" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-1 form-group-remove">
            <button type="button" class="btn btn-transparent removeButton"><i class="fal fa-trash"></i></button>
        </div>
    </fieldset>
</formUtil:dynamicComplexInputs>


<fieldset>
    <div class="form-group text-center">
        <button type="submit" class="btn btn-default btn-lg"><g:message code="admin.createUser.submit"/></button>
    </div>
</fieldset>