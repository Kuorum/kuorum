<input type="hidden" name="user.id" value="${command.user.id}"/>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="name" required="true" showLabel="true"/>
    </div>
</fieldset>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="email" required="true" showLabel="true"/>
    </div>
</fieldset>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="alias" showLabel="true" helpBlock="https://kuorum.org/${command.alias?:'alias'}"/>
    </div>
</fieldset>

<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="language"/>
    </div>
</fieldset>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:regionInput command="${command}" field="homeRegion" showLabel="true"/>
    </div>
</fieldset>
<fieldset class="row">
    <div class="form-group col-md-2">
        <formUtil:input command="${command}" field="phonePrefix" showLabel="true"/>
    </div>
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="phone" showLabel="true"/>
    </div>
</fieldset>