<r:require modules="forms"/>
<formUtil:dynamicComplexInputs
        command="${extraInfoCommand}"
        field="extraInfo"
        listClassName="kuorum.web.commands.payment.contact.ContactExtraInfoValuesCommand"
        cssParentContainer="contact-extraInfo-dynamic-fields"
        customRemoveButton="true"
        appendLast="true"
        formId="extraInfoContact">
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-5">
            <formUtil:input field="key" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="false"/>
        </div>
        <div class="form-group col-md-5">
            <formUtil:input field="value" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="false"/>
        </div>
        <div class="col-md-2 text-right">
            <button type="button" class="btn btn-transparent removeButton"><i class="fal fa-trash"></i></button>
        </div>
    </fieldset>
</formUtil:dynamicComplexInputs>