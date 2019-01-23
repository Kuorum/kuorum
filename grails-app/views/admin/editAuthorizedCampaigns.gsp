<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.authorizedCampaigns.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminAuthorizedCampaigns']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.authorizedCampaigns.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.authorizedCampaigns.subtitle"/></h3>
</content>
<content tag="mainContent">
    <g:form method="POST" mapping="adminAuthorizedCampaigns" name="editAuthorizedCampaignsForm" role="form">
        <g:render template="domainAuthorizedCampaigns" model="[campaignRoles:campaignRoles,userRoles:userRoles, globalAuthoritiesCommand:globalAuthoritiesCommand]"/>
    </g:form>
</content>