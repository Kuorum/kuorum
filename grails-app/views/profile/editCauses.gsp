<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="dashboard.userProfile.causes"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profileCauses', menu:menu]"/>

</content>
<content tag="titleContent">
    <h1><g:message code="profile.menu.profileCauses"/></h1>
    <h3><g:message code="profile.menu.profileCauses.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm form="causesForm" bean="${command}"/>
    <g:form method="POST" mapping="profileCauses" name="causesForm" role="form">
        <g:render template="formCauses" model="[command:command]"/>
    </g:form>
</content>
