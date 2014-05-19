<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.showUserPosts"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.profileMyPosts.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.profileMyPosts.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileNotifications', menu:menu]"/>

</content>
<content tag="mainContent">
    <h1><g:message code="profile.profileMyPosts.title"/></h1>
    Mis posts
</content>
