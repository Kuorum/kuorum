<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<formUtil:dynamicComplexInputs
        command="${command}"
        field="externalPoliticianActivities"
        listClassName="kuorum.users.extendedPoliticianData.ExternalPoliticianActivity"
        formId="externalActivitiesForm">
    <fieldset class="row">
        <div class="form-group col-md-12">
            <formUtil:input field="title" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-4">
            <formUtil:date field="date" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>
        <div class="form-group col-md-8">
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
    <div class="form-group">
        <div class="col-xs-5 col-xs-offset-1">
            <button type="submit" class="btn btn-default">Submit</button>
        </div>
    </div>
</fieldset>
