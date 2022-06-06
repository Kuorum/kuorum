<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.setCustomDomain.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminEditDomainEmailSender']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.setCustomDomain.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.setCustomDomain.subtitle"/></h3>
</content>
<content tag="mainContent">

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="customDomainForm"/>
    <g:form method="POST" mapping="adminEditDomainEmailSender" name="customDomainForm" role="form" class="submitOrangeButton">
        <div class="box-ppal-section">
            <h4 class="box-ppal-section-title">
                Custom email sender
            </h4>
        </div>
        <div class="box-ppal-section">
            <g:if test="${requestState}">
                <fieldset aria-live="polite" class="row validate" style="text-align: left">
                    <div class="col-xs-12 valid">
                        <strong><span>Sender with web domain:</span></strong><i class="fas fa-check fa-2x"></i>
                    </div>
                </fieldset>
            </g:if>
            <g:else>
                <fieldset aria-live="polite" class="row">
                    <div class="form-group col-md-6">
                        <formUtil:input type="text" command="${command}" field="mandrillAppKey" showLabel="true"/>
                    </div>
                </fieldset>

                <div class="box-ppal-section">
                    <fieldset aria-live="polite" class="form-group text-center">
                        <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-orange btn-lg">
                    </fieldset>
                </div>
            </g:else>
        </div>
    </g:form>
</content>