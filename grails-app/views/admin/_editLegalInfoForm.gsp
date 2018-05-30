<r:require modules="forms"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.legalInfo.label"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="address" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="city" showLabel="true" />
        </div>
    </fieldset>

    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="country" showLabel="true" />
        </div>
    </fieldset>
    %{--<h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.colors.label"/></h4>--}%
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="domainOwner" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="fileResponsibleName" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="fileResponsibleEmail" showLabel="true" />
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="fileName" showLabel="true" />
        </div>
        <div class="form-group col-md-12">
            <formUtil:textArea command="${command}" field="filePurpose" showLabel="true" />
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