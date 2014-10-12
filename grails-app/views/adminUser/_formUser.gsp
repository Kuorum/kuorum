<div class="form-group">
    <div class="row">
        <div class="col-xs-4">
            <formUtil:input command="${command}" field="name" required="true" autocomplete="off"/>
        </div>
        <div class="col-xs-4">
            <formUtil:input command="${command}" field="email" type="email" required="true" autocomplete="off"/>
        </div>
        <div class="col-xs-4">
            <formUtil:input command="${command}" field="password" type="password" required="true"/>
        </div>
    </div>

</div>
<div class="form-group">
    <div class="row">
        <div class="col-xs-6">
            <formUtil:selectDomainObject command="${command}" field="institution" values="${institutions}" />
        </div>
        <div class="col-xs-6">
            <formUtil:selectDomainObject command="${command}" field="politicalParty" values="${politicalParties}" />
        </div>
    </div>
</div>
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
<div class="form-group">
    <div class="row">
        <div class="col-xs-6">
            <formUtil:radioEnum command="${command}" field="userType"/>
        </div>
        <div class="col-xs-6">
            <formUtil:radioEnum command="${command}" field="gender"/>
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <div class="col-xs-4">
            <formUtil:input
                    command="${command}"
                    field="postalCode"
                    required="true"
                    type="number"
                    maxlength="5"
            />
        </div>
        <div class="col-xs-4 userData">
            <formUtil:selectEnum command="${command}" field="workingSector"/>
        </div>
        <div class="col-xs-4 userData">
            <formUtil:selectEnum command="${command}" field="studies"/>
        </div>
        <div class="col-xs-4 organizationData">
            <formUtil:selectEnum command="${command}" field="enterpriseSector"/>
        </div>
    </div><!-- /.row -->
</div><!-- /.form-group -->
<div class="form-group">
    <span class="span-label"><g:message code="customRegister.step1.form.birthday.label"/></span>
    <div class="row">
        <div class="col-xs-4">
            <formUtil:input
                    command="${command}"
                    field="day"
                    labelCssClass="sr-only"
                    required="true"
                    type="number"
                    maxlength="2"
            />
        </div>
        <div class="col-xs-4">
            <formUtil:input
                    command="${command}"
                    field="month"
                    labelCssClass="sr-only"
                    required="true"
                    type="number"
                    maxlength="2"
            />
        </div>
        <div class="col-xs-4">
            <formUtil:input
                    command="${command}"
                    field="year"
                    labelCssClass="sr-only"
                    required="true"
                    type="number"
                    maxlength="4"
            />
        </div>
    </div>
</div>
<div class="form-group">
    <formUtil:textArea command="${command}" field="bio"/>
</div>
<div class="form-group">
    <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.USER_AVATAR}"/>
</div>
<div class="form-group">
    <formUtil:editImage command="${command}" field="imageProfile" fileGroup="${kuorum.core.FileGroup.USER_PROFILE}"/>
</div>
