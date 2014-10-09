<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.createLaw.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.createLaw.title"/>,
    </h1>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminListRegions', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.createLaw.title"/></h1>
    <ul>
        <g:each in="${regions}" var="region">
            <li><g:link controller="adminRegion" action="editRegion" params="[iso3166_2:region.iso3166_2]">${region.name}</g:link> </li>
        </g:each>
    </ul>
</content>
