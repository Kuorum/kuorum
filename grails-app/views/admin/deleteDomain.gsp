<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.deleteDomain.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminDomainConfigUploadLogo']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.deleteDomain.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.deleteDomain.subtitle"/></h3>
</content>
<content tag="mainContent">

    <formUtil:validateForm form="adminDomainDeleteForm" bean="${command}"/>
    <g:form method="POST" mapping="adminDomainDelete" name="adminDomainDeleteForm" role="form">
        <div class="box-ppal-section">
            <h4 class="box-ppal-section-title"><g:message code="admin.menu.domainConfig.deleteDomain.title"/></h4>

            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:input command="${command}" field="domainName" showLabel="true" />
                </div>
                <div class="form-group col-md-6">
                    <formUtil:password command="${command}" field="password" showLabel="true" />
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
    </g:form>
</content>
