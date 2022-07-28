<%--
  Created by IntelliJ IDEA.
  User: FCT
  Date: 27/07/2022
  Time: 16:39
--%>

<%@ page contentType="text/html;charset=UTF-8" import="springSecurity.KuorumRegisterCommand" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profileEditAccountDetails"/></title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config"/>
</head>

<body>

<content tag="leftMenu">
    <g:render template="leftMenuAccount" model="[activeMapping: 'profilePrivateFiles', menu: menu]"/>
    <r:require modules="forms"/>
</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.adminPrivateFiles.title"/></h1>

    <h3><g:message code="profile.menu.adminPrivateFiles.subtitle"/></h3>
</content>
<content tag="mainContent">
    <div>
        <formUtil:uploadContactFiles contact="${contact}" adminContact="true"
                                     label="${g.message(code: 'customRegister.fillProfile.files.uploadContactFiles.label')}"/>
    </div>
</content>
</body>
</html>