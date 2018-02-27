<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editSocialNetwork.title" args="[user.name]"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="editorUserMenu" model="[user:user]"/>
</content>

<content tag="titleContent">
    <h1><g:message code="admin.editSocialNetwork.title" args="[user.name]"/></h1>
    <h3><g:message code="profile.socialNetworks.title" args="[user.name]"/></h3>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="accountForm"/>
    <g:form method="POST" mapping="editorEditSocialNetwork" params="${user.encodeAsLinkProperties()}" name="accountForm" role="form" class="submitOrangeButton">
        <g:render template="/profile/formSocialNetworks" model="[user:user, command:command]"/>
    </g:form>
</content>
