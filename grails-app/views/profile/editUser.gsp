<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.editUser"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="edit-user" />
    <r:require module="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileEditUser', menu:menu]"/>
</content>
<content tag="titleContent">
    <h1><g:message code="profile.menu.profileEditUser"/></h1>
    <h3><g:message code="profile.menu.profileEditUser.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="config1" dirtyControl="true"/>
    <g:form method="POST" mapping="profileEditUser" name="config1" role="form">
        <g:render template="formEditUser" model="[user:user, command:command]"/>
    </g:form>
</content>
