<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changePassword"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.editUser.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.changePassword.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileEditUser', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="profile.changePassword.title"/></h1>
    <formUtil:validateForm form="config2" bean="${command}"/>
    <g:form method="POST" mapping="profileChangePass" name="config2" role="form">
        <div class="form-group">
            <formUtil:input type="password" command="${command}" field="originalPassword"/>
        </div>
        <div class="form-group">
            <formUtil:input type="password" command="${command}" field="password"/>
        </div>
        <div class="form-group">
            <formUtil:input type="password" command="${command}" field="password2"/>
        </div>
        <div class="form-group">
            <input type="submit" value="Guardar y continuar" class="btn btn-grey btn-lg">
            <a href="#" class="cancel" tabindex="19">Cancelar</a>
        </div>
    </g:form>
</content>
