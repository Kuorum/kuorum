<r:require modules="forms"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.landingSettings.label"/></h4>

    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="slogan" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="subtitle" showLabel="true" />
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:textArea command="${command}" field="domainDescription" showLabel="true" />
        </div>
    </fieldset>
</div>
<div class="box-ppal-section">
    <fieldset>
        <div class="form-group text-center">
            <button type="submit" class="btn btn-default btn-lg"><g:message code="admin.createUser.submit"/></button>
        </div>
    </fieldset>
</div>