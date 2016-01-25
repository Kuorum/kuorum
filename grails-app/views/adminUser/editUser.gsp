<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editUser.title" args="[user.name]"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.editUser.title" args="[user.name]"/>,
    </h1>
    <g:render template="adminUserMenu" model="[user:user]"/>

    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'', menu:menu]"/>
</content>

<content tag="mainContent">
    <h1><g:message code="admin.editUser.title" args="[user.name]"/></h1>
    <formUtil:validateForm bean="${command}" form="editUser"/>
    <g:form method="POST" mapping="adminEditUser" params="${user.encodeAsLinkProperties()}" name="editUser" role="form">
        <g:render template="/profile/formEditUser" model="[user: user, command:command]"/>
    </g:form>
</content>
