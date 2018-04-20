<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editUser.title" args="[user.name]"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="editorUserMenu" model="[user:user]"/>
</content>

<content tag="titleContent">
    <h1><g:message code="admin.editUser.title" args="[user.name]"/></h1>
    <h3><g:message code="admin.menu.user.editAccount" args="[user.name]"/></h3>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="accountForm"/>
    <g:form method="POST" mapping="editorKuorumAccountEdit" params="${user.encodeAsLinkProperties()}" name="accountForm" role="form" class="submitOrangeButton">
        <div class="box-ppal-section">
            <g:render template="/profile/accountDetailsForm" model="[command:command]"/>
        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>
    </g:form>
</content>
