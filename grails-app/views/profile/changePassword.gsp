<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changePassword"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileChangePass', menu:menu]"/>

</content>

<content tag="mainContent">
    <formUtil:validateForm form="config2" bean="${command}"/>
    <g:form method="POST" mapping="profileChangePass" name="config2" role="form" class="box-ppal">
        %{--<h1><g:message code="profile.changePassword.title"/></h1>--}%
        <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input type="password" command="${command}" field="originalPassword" showLabel="true"/>
        </div>
        </fieldset>
        <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input type="password" command="${command}" field="password" showLabel="true"/>
        </div>
        </fieldset>
        <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input type="password" command="${command}" field="password2" showLabel="true"/>
        </div>
        </fieldset>
        <fieldset class="form-group text-right">
        <div class="form-group">
            <a href="#" class="cancel" tabindex="19"><g:message code="profile.emailNotifications.cancel"/></a>
            <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-grey btn-lg">
        </div>
        </fieldset>
    </g:form>
</content>
