<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.relevantCampaigns.title"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminDomainConfigRelevantCampagins']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.relevantCampaigns.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.relevantCampaigns.subtitle"/></h3>
</content>
<content tag="mainContent">
    <g:form method="POST" mapping="adminDomainConfigRelevantCampagins" name="editRelevantCampagins" role="form">
        <g:render template="domainRelevantCampaignsForm" model="[domainCampaignsId:domainCampaignsId,adminCampaigns:adminCampaigns]"/>
    </g:form>
</content>