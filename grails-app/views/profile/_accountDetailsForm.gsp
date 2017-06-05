<input type="hidden" name="user.id" value="${command?.user?.id}"/>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="name" required="true" showLabel="true" showCharCounter="false"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="surname" required="true" showLabel="true" showCharCounter="false"/>
    </div>
</fieldset>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="email" required="true" showLabel="true"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="alias" showLabel="true" showCharCounter="false" helpBlock="https://kuorum.org/${command.alias?:'alias'}"/>
    </div>
</fieldset>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="language"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:selectTimeZone command="${command}" field="timeZoneId"/>
    </div>

</fieldset>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:regionInput command="${command}" field="homeRegion" showLabel="true"/>
    </div>
    <div class="form-group col-md-2">
        <formUtil:input command="${command}" field="phonePrefix" showLabel="true"/>
    </div>
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="phone" showLabel="true" type="number"/>
    </div>
</fieldset>