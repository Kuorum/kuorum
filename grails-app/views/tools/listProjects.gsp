<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="project.list.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.adminPrincipal.title"/>,
    </h1>
    %{--TODO: Menú lateral izquierdo "Herramientas"--}%
    %{--<g:render template="/admin/adminMenu" model="[activeMapping:'adminPrincipal', menu:menu]"/>--}%

</content>

<content tag="mainContent">
    <div id="projectsList">
        <g:set var="urlLoadMore" value="${createLink(mapping: 'projectListOfUsers', params: [template: 'listProjects'])}" />
        <g:render template="projects" model="[projects:projects, order: order, sort: sort, published: published, max: max, offset: offset, totalProjects: totalProjects, publishedProjects: publishedProjects, draftProjects: draftProjects, seeMore: seeMore, urlLoadMore: urlLoadMore]"/>
    </div>
</content>
