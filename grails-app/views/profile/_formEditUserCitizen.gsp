<%@ page import="kuorum.core.model.Gender" %>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:date command="${command}" field="birthday" showLabel="true"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:radioEnum command="${command}" field="gender"/>
    </div>
</fieldset>

<fieldset class="row userData">
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="workingSector"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="studies"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="position" showLabel="true"/>
    </div>
</fieldset>

<fieldset class="row organizationData">
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="enterpriseSector"/>
    </div>
</fieldset>