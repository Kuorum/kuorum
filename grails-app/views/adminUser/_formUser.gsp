<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="name" required="true" autocomplete="off" showLabel="true"/>
    </div>
</fieldset>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="email" type="email" required="true" autocomplete="off" showLabel="true"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="password" type="password" required="true" showLabel="true"/>
    </div>
</fieldset>

<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:selectNation command="${command}" field="country"/>
    </div>
    <div class="form-group col-md-6 postal">
        <formUtil:input
                command="${command}"
                field="postalCode"
                required="true"
                type="number"
                maxlength="5"
                showLabel="true"
        />
    </div>
</fieldset>

<fieldset class="row">
    <div class="col-xs-6">
        <formUtil:selectDomainObject command="${command}" field="institution" values="${institutions}" />
    </div>
    <div class="col-xs-6">
        <formUtil:selectDomainObject command="${command}" field="politicalParty" values="${politicalParties}" />
    </div>
</fieldset>

<div class="form-group clearfix">
    <div class="row">
        <div class="col-xs-6">
            <formUtil:checkBox command="${command}" field="verified"/>
        </div>
        <div class="col-xs-6">
            <formUtil:checkBox command="${command}" field="enabled"/>
        </div>
    </div>
</div>
<fieldset class="row">
    <div class="col-xs-6">
        <formUtil:radioEnum command="${command}" field="userType"/>
    </div>
    <div class="col-xs-6">
        <formUtil:radioEnum command="${command}" field="gender"/>
    </div>
</fieldset>

<fieldset class="row userData">
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="studies"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="workingSector"/>
    </div>
</fieldset>


<fieldset class="row organizationData">
    <div class="form-group col-md-6">
        <formUtil:selectEnum command="${command}" field="enterpriseSector"/>
    </div>
</fieldset>

<fieldset class="row">
    <div class="form-group col-md-6 nacimiento">
        <formUtil:selectBirdthYear command="${command}" field="year"/>
    </div>
</fieldset>

<fieldset class="form-group">
    <formUtil:textArea command="${command}" field="bio"/>
</fieldset>
<fieldset class="form-group image perfil" data-multimedia-switch="on" data-multimedia-type="IMAGE">
    <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.USER_AVATAR}"/>
</fieldset>

<fieldset class="form-group image" data-multimedia-switch="on" data-multimedia-type="IMAGE">
    <formUtil:editImage command="${command}" field="imageProfile" fileGroup="${kuorum.core.FileGroup.USER_PROFILE}"/>
</fieldset>


<fieldset class="form-group interest">
    <formUtil:selectMultipleCommissions command="${command}" field="commissions"/>
</fieldset>
