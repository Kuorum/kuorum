<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.editUser"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileEditCommissions', menu:menu]"/>

</content>
<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="config1" />
    <g:form method="POST" mapping="profileEditCommissions" name="config1" role="form">

        <fieldset class="form-group interest">
            <formUtil:selectMultipleCommissions command="${command}" field="commissions"/>
        </fieldset>

        <fieldset class="form-group text-right">
            <a href="#" class="cancel" tabindex="19"><g:message code="profile.emailNotifications.cancel"/></a>
            <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-grey btn-lg">
        </fieldset>
    </g:form>
</content>
