<r:require modules="forms"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.landingSettings.label"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="name" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:selectEnum command="${command}" field="language" showLabel="true" />
        </div>
    </fieldset>

    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.colors.label"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="mainColor" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="mainColorShadowed" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="secondaryColor" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="secondaryColorShadowed" showLabel="true" />
        </div>
    </fieldset>
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.style.label"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:selectEnum command="${command}" field="titleWebFont" showLabel="true" />
        </div>
    </fieldset>

    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.social.label"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="facebook" cssIcon="fab fa-facebook"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="twitter" cssIcon="fab fa-twitter"/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="youtube" cssIcon="fab fa-youtube-square"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="linkedIn" cssIcon="fab fa-linkedin-in"/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="instagram" cssIcon="fab fa-instagram"/>
        </div>
    </fieldset>
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.validation.label"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="validation" showLabel="true" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:checkBox command="${command}" field="validationPhone" showLabel="true" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="smsDomainName" showLabel="true" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
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