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
    <formUtil:validateForm bean="${command}" form="accountForm"/>
    <g:form method="POST" mapping="adminKuorumAccountEdit" params="${user.encodeAsLinkProperties()}" name="accountForm" role="form" class="box-ppal">
        <input type="hidden" name="user.id" value="${command.user.id}"/>
        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="alias" required="true" autocomplete="off" showLabel="true"/>
            </div>
            <div class="form-group col-md-6">
                <formUtil:checkBox command="${command}" field="active" showLabel="true"/>
            </div>
        </fieldset>

        <div class="form-group">
            <input type="submit" value="${message(code:'admin.createProject.submit')}" class="btn btn-grey btn-lg">
            <a href="#" class="cancel" tabindex="19">Cancelar</a>
        </div>
    </g:form>
</content>
