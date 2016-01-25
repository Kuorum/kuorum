<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profilePoliticianProfessionalDetails"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profilePoliticianQuickNotes', menu:menu]"/>

</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.profilePoliticianQuickNotes"/></h1>
    <h3><g:message code="profile.menu.profilePoliticianQuickNotes.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm form="quickNotesForm" bean="${command}" dirtyControl="true"/>
    <g:form method="POST" mapping="profilePoliticianQuickNotes" name="quickNotesForm" role="form">
        <g:render template="formQuickNotes" model="[command:command]"/>
    </g:form>
</content>
