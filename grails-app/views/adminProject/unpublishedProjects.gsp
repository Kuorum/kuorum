<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.changeEmail"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.unpublishedProjects.title"/>,
    </h1>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminUnpublishedProjects', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.unpublishedProjects.title"/></h1>
    <ul>
    <g:each in="${projects}" var="project">
        <li><g:link mapping="adminEditProject" params="${project.encodeAsLinkProperties()}">${project.hashtag}</g:link> </li>
    </g:each>
    </ul>
</content>
