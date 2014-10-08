<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changeEmail"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.changeEmail.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.changeEmail.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileChangeEmail', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="profile.changeEmail.success.title"/></h1>
    <p><g:message code="profile.changeEmail.success.description"  args="[newEmail]"/></p>
    <g:if env="development">
        <p><a href="${confirmationLink}">Link de confirmacion para desarrollo</a></p>
    </g:if>
</content>
