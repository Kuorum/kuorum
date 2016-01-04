<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profilePoliticianExternalActivity"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profilePoliticianCauses', menu:menu]"/>

</content>

<content tag="mainContent">
    <formUtil:validateForm form="causesForm" bean="${command}"/>
    <g:form method="POST" mapping="profilePoliticianCauses" name="causesForm" role="form">
        <g:render template="formPoliticianCauses" model="[command:command]"/>
    </g:form>
</content>
