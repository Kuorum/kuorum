<%@ page import="kuorum.core.model.Gender" %>
<fieldset aria-live="polite" class="row">
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="gender"/>
    </div>
    <div class="form-group col-md-6 userData">
        <formUtil:date command="${command}" field="birthday" showLabel="true" datePickerType="birthDate"/>
    </div>
</fieldset>
