<r:require modules="forms"/>
<input type="hidden" name="user.id" value="${user?.id}"/>
<fieldset aria-live="polite" class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="name" required="true" showLabel="true" showCharCounter="false"/>
    </div>

    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="surname" required="true" showLabel="true" showCharCounter="false"/>
    </div>
</fieldset>
<fieldset aria-live="polite" class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="email" required="true" showLabel="true"/>
    </div>
</fieldset>
<fieldset aria-live="polite" class="row">
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="language"/>
    </div>

    <div class="form-group col-md-6">
        <formUtil:selectTimeZone command="${command}" field="timeZoneId"/>
    </div>
</fieldset>
<fieldset aria-live="polite" class="row">
    <div class="form-group col-md-12">
        <formUtil:textArea command="${command}" field="bio" showLabel="true" texteditor="texteditor"/>
    </div>
</fieldset>
