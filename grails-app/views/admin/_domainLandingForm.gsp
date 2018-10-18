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
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.footerLinks.label"/></h4>

    <fieldset>
        <formUtil:dynamicComplexInputs
                command="${command}"
                field="footerLinks"
                listClassName="kuorum.web.commands.LinkCommand"
                cssParentContainer="profile-dynamic-fields"
                customRemoveButton="true"
                appendLast="true"
                formId="adminDomainConfigLandingForm">
            <fieldset class="row">
                <div class="form-group col-md-5">
                    <formUtil:input field="title" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
                <div class="form-group col-md-5">
                    <formUtil:url field="url" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
                <div class="form-group col-md-1 form-group-remove">
                    <button type="button" class="btn btn-transparent removeButton"><i class="fa fa-trash"></i></button>
                </div>
            </fieldset>
        </formUtil:dynamicComplexInputs>
    </fieldset>
</div>
<div class="box-ppal-section">
    <fieldset>
        <div class="form-group text-center">
            <button type="submit" class="btn btn-default btn-lg"><g:message code="default.save"/></button>
        </div>
    </fieldset>
</div>