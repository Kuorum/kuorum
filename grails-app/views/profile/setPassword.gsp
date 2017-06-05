<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changePassword"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuAccount" model="[user:user, activeMapping:'profileChangePass', menu:menu]"/>
</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.changePass"/></h1>
    <h3><g:message code="profile.menu.changePass.subtitle"/></h3>
</content>

<content tag="mainContent">
    <formUtil:validateForm form="changePassword" bean="${command}"/>
    <g:form method="POST" mapping="profileSetPass" name="changePassword" role="form" class="submitOrangeButton">
        <input type="hidden" name="username" value="${sec.username()}"
        <div class="box-ppal-section">
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:password command="${command}" field="password" required="true" showLabel="true"/>
                </div>
            </fieldset>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:password command="${command}" field="password2" required="true" showLabel="true"/>
                </div>
            </fieldset>
        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>
    </g:form>
</content>
