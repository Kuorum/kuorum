<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changeEmail"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileChangeEmail', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="profile.changeEmail.title"/></h1>
    <formUtil:validateForm form="config2" bean="${command}"/>
    <g:form method="POST" mapping="profileChangeEmail" name="config2" role="form">
        <div class="form-group">
            <formUtil:input type="email" command="${command}" field="email"/>
        </div>
        <div class="form-group">
            <input type="submit" value="Guardar y continuar" class="btn btn-grey btn-lg">
        </div>
    </g:form>
</content>
