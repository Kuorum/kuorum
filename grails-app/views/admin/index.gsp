<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changeEmail"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.adminPrincipal.title"/>,
    </h1>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminPrincipal', menu:menu]"/>

</content>

<content tag="titleContent">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <h3>Admin domain page</h3>
</content>
<content tag="mainContent">
    <p>Section for domain administration</p>
</content>
