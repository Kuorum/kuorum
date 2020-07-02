<r:require modules="forms"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.validation.label"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="validationCensus" showLabel="true" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="validationCode" showLabel="true" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="validationPhone" showLabel="true" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
        </div>

    </fieldset>
    <fieldset class="row">

        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="smsDomainName" showLabel="true" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
        </div>
        <div class="form-group col-md-3">
            <formUtil:selectPhonePrefix command="${command}" field="defaultPhonePrefix" showLabel="true" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
        </div>
    </fieldset>
</div>
<div class="box-ppal-section">
    <fieldset>
        <div class="form-group text-center">
            <button type="submit" class="btn btn-default btn-lg"><g:message code="default.save"/></button>
        </div>
    </fieldset>
</div>