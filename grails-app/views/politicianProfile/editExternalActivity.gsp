<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profilePoliticianExternalActivity"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profilePoliticianExternalActivity', menu:menu]"/>

</content>
<content tag="titleContent">
    <h1><g:message code="profile.menu.profilePoliticianExternalActivity"/></h1>
    <h3><g:message code="profile.menu.profilePoliticianExternalActivity.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm form="externalActivitiesForm" bean="${command}" dirtyControl="true"/>
    <g:form method="POST" mapping="profilePoliticianExternalActivity" name="externalActivitiesForm" role="form">
        <g:render template="formExternalActivity" model="[command:command]"/>
    </g:form>
</content>
