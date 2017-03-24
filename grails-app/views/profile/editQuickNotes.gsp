<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profileProfessionalDetails"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profileQuickNotes', menu:menu]"/>

</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.profileQuickNotes"/></h1>
    <h3><g:message code="profile.menu.profileQuickNotes.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm form="quickNotesForm" bean="${command}" dirtyControl="true"/>
    <g:form method="POST" mapping="profileQuickNotes" name="quickNotesForm" role="form">
        <g:render template="formQuickNotes" model="[command:command]"/>
    </g:form>
</content>
