<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editUser.title" args="[command.politician.name]"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.editUser.title" args="[command.politician.name]"/>,
    </h1>
    <g:render template="/editorUser/adminUserMenu" model="[user:command.politician]"/>
    <g:render template="/admin/adminMenu" model="[activeMapping:'editorEditPoliticianQuickNotes', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.menu.user.editPersonalNotes" args="[command.politician.name]"/></h1>
    <formUtil:validateForm form="externalActivitiesForm" bean="${command}"/>
    <g:form method="POST" mapping="editorEditPoliticianQuickNotes" params="${command.politician.encodeAsLinkProperties()}" name="externalActivitiesForm" role="form">
        <g:render template="/politicianProfile/formQuickNotes" model="[command:command]"/>
    </g:form>
</content>
