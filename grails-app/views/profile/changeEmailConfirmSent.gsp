<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changeEmail"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuAccount" model="[user:user, activeMapping:'profileChangeEmail', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="profile.changeEmail.success.title"/></h1>
    <p><g:message code="profile.changeEmail.success.description"  args="[newEmail]"/></p>
    <g:if env="development">
        <p><a href="${confirmationLink}">Link de confirmacion para desarrollo</a></p>
    </g:if>
</content>
