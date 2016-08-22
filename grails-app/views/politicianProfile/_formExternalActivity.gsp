<r:require modules="forms"/>
<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<formUtil:dynamicComplexInputs
        command="${command}"
        field="externalPoliticianActivities"
        listClassName="kuorum.users.extendedPoliticianData.ExternalPoliticianActivity"
        cssParentContainer="profile-dynamic-fields"
        formId="externalActivitiesForm">
    <fieldset class="row">
        <div class="form-group col-md-12">
            <formUtil:input field="title" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:date field="date" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:url field="link" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input field="actionType" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:input field="outcomeType" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
    </fieldset>
</formUtil:dynamicComplexInputs>


<fieldset>
    <div class="form-group text-center">
        <button type="submit" class="btn btn-default btn-lg"><g:message code="admin.createUser.submit"/></button>
    </div>
</fieldset>
