<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.socialNetworks"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.socialNetworks.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.socialNetworks.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileSocialNetworks', menu:menu]"/>

</content>

<content tag="mainContent">
    <H1> Perfil de ${user.name}</H1>
    Redes sociales
</content>
