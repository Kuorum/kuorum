<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editUser.title" args="[user.name]"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="editorUserMenu" model="[user:user]"/>
</content>
<contetn tag="titleContent">
    <h1><g:message code="admin.editUser.title" args="[user.name]"/></h1>
</contetn>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="editUser"/>
    <g:form method="POST" mapping="editorEditUserProfile" params="${user.encodeAsLinkProperties()}" name="editUser" role="form">
        <g:render template="/profile/formEditUser" model="[user: user, command:command]"/>
    </g:form>
</content>
