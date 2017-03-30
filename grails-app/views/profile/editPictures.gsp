<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.editUser"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="edit-user" />
    <r:require module="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profilePictures', menu:menu]"/>
</content>
<content tag="titleContent">
    <h1><g:message code="profile.menu.profilePictures"/></h1>
    <h3><g:message code="profile.menu.profileEditUser.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="config1" dirtyControl="true"/>
    <g:form method="POST" mapping="profilePictures" name="config1" role="form">
        <g:render template="formEditPictures" model="[command:command]"/>
    </g:form>
</content>