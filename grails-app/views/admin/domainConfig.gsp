<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.title"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminDomainConfig']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm form="domainConfigForm" bean="${command}" dirtyControl="true"/>
    <g:form method="POST" mapping="adminDomainConfig" name="domainConfigForm" role="form">
        <g:render template="domainConfigForm" model="[command:command]"/>
    </g:form>
</content>
