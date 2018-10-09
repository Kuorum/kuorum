<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.editLanding.title"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminDomainConfigLanding']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.editLanding.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.editLanding.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm form="adminDomainConfigLandingForm" bean="${command}" dirtyControl="true"/>
    <g:form method="POST" mapping="adminDomainConfigLanding" name="adminDomainConfigLandingForm" role="form">
        <g:render template="domainLandingForm" model="[command:command]"/>
    </g:form>
</content>
