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
    <p><g:render template="switchUser" model="[user:user]"/></p>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'', menu:menu]"/>
</content>

<content tag="mainContent">
    <h1><g:message code="admin.editUser.title" args="[user.name]"/></h1>
    <formUtil:validateForm bean="${command}" form="editUser"/>
    <g:form method="POST" mapping="adminEditUser" params="${user.encodeAsLinkProperties()}" name="editUser" role="form">
        <g:render template="formUser" model="[command:command, institutions:institutions, parliamentaryGroups:parliamentaryGroups]"/>
        <div class="form-group">
            <input type="submit" value="${message(code:'admin.createLaw.submit')}" class="btn btn-grey btn-lg">
            <a href="#" class="cancel" tabindex="19">Cancelar</a>
        </div>
    </g:form>
</content>
