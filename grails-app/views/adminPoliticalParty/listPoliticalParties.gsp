<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.createProject.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.createProject.title"/>,
    </h1>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'listCreatePoliticalParties', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.listRegion.title"/></h1>
    <ul>
        <g:each in="${politicalParties}" var="politicalParty">
            <li><g:link  mapping="adminEditPoliticalParty" params="[id:politicalParty.id]">${politicalParty.name}</g:link> </li>
        </g:each>
    </ul>
</content>
