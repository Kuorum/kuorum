<g:set var="disabledForAdmins"
       value="${grails.plugin.springsecurity.SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN")}"/>
<r:require modules="forms,domainValidation"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.configuration.label"/></h4>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="isSocialNetwork" showLabel="true"
                               disabled="${!disabledForAdmins}"/>
        </div>
    </fieldset>

    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="isUserProfileExtended" showLabel="true"
                               disabled="${!disabledForAdmins}"/>
        </div>
    </fieldset>
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.contentPrivacy.label"/></h4>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-12">
            <formUtil:radioEnum command="${command}" field="domainPrivacy" showLabel="false" multiLine="true"
                                disabled="${!disabledForAdmins}"/>
        </div>
    </fieldset>
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.validation.label"/></h4>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="validationCensus" showLabel="true"
                               disabled="${!disabledForAdmins}"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="validationTokenMail" showLabel="true"
                               disabled="${!disabledForAdmins}"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="validationCode" showLabel="true"
                               disabled="${!disabledForAdmins}"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="validationPhone" showLabel="true"
                               disabled="${!disabledForAdmins}"/>
        </div>
    </fieldset>

    <fieldset aria-live="polite" class="row">

        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="smsDomainName" showLabel="true"
                            disabled="${!disabledForAdmins}"/>
        </div>

        <div class="form-group col-md-6">
            <formUtil:selectPhonePrefix command="${command}" field="defaultPhonePrefix" showLabel="true"
                                        disabled="${!disabledForAdmins}"/>
        </div>
    </fieldset>
</div>

<div class="box-ppal-section">
    <fieldset aria-live="polite">
        <div class="form-group text-center">
            <button type="submit" class="btn btn-default btn-lg"><g:message code="default.save"/></button>
        </div>
    </fieldset>
</div>