<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.user.editPoliticianExperience"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.editUser.title" args="[command.politician.name]"/>,
    </h1>
    <g:render template="/editorUser/editorUserMenu" model="[user:command.politician]"/>
</content>

<content tag="mainContent">
    <h1><g:message code="admin.menu.user.editPoliticianExperience" args="[command.politician.name]"/></h1>
    <formUtil:validateForm form="politicianExperienceForm" bean="${command}"/>
    <g:form method="POST" mapping="editorEditPoliticianExperience" params="${command.politician.encodeAsLinkProperties()}" name="politicianExperienceForm" role="form">
        <g:render template="/politicianProfile/formPoliticianExperience" model="[command:command]"/>
    </g:form>
</content>
