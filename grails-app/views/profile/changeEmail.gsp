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
    <formUtil:validateForm form="config2" bean="${command}"/>
    <g:form method="POST" mapping="profileChangeEmail" name="config2" role="form" class="box-ppal">
        <h1><g:message code="profile.changeEmail.title"/></h1>
        <fieldset class="form-group interest">
        <div class="form-group col-md-6">
            <formUtil:input type="email" command="${command}" field="email" showLabel="true"/>
        </div>
        </fieldset>
        <fieldset class="form-group interest">
        <div class="form-group text-right">
            <input type="submit" value="Guardar y continuar" class="btn btn-grey btn-lg">
        </div>
        </fieldset>
    </g:form>
</content>
