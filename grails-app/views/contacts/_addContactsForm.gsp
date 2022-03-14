<r:require modules="forms"/>
<formUtil:dynamicComplexInputs
        command="${command}"
        field="newContactCommands"
        listClassName="kuorum.web.commands.payment.contact.NewContactCommand"
        cssParentContainer="profile-dynamic-fields"
        customRemoveButton="true"
        appendLast="true"
        formId="surveyInitAddContactsForm">
    <fieldset class="row">
        <input type="hidden" name="${prefixField}.conditions" value="true"/>

        <div class="form-group col-md-5">
            <formUtil:input field="name" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>

        <div class="form-group col-md-5">
            <formUtil:input field="email" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
        </div>

        <div class="form-group col-md-1 form-group-remove">
            <button type="button" class="btn btn-transparent removeButton"><i class="fal fa-trash"></i></button>
        </div>
    </fieldset>
</formUtil:dynamicComplexInputs>