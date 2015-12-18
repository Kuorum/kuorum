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
    <g:form method="POST" mapping="adminEditUser" params="${user.encodeAsLinkProperties()}" name="editUser" role="form" class="box-ppal">
        <g:render template="formUser" model="[command:command, institutions:institutions, politicalParties:politicalParties]"/>
        <div class="form-group">
            <input type="submit" value="${message(code:'admin.createProject.submit')}" class="btn btn-grey btn-lg">
            <a href="#" class="cancel" tabindex="19">Cancelar</a>
        </div>
    </g:form>
</content>
