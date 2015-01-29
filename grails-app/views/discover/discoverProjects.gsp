<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.discover.projects"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <parameter name="idMainContent" value="search-results" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="discover.menu.discover"/>
    </h1>
    %{--<p><g:message code="discover.menu.projects.description"/></p>--}%
    <g:render template="discoverLeftMenu" model="[activeMapping:'discoverProjects', dynamicDiscoverProjects:dynamicDiscoverProjects]"/>

</content>

<content tag="mainContent">
    <div class="clearfix">
        <h1>
            <g:message code="discover.title.discover.projects"/>
        </h1>
    </div>
    <ul id="project-list-id">
        <g:render template="discoverProjectList" model="[projects:projects]"/>
    </ul>

    <nav:loadMoreLink
            mapping="discoverProjects"
            mappingParams="[iso3166_2:params.iso3166_2]"
            parentId="project-list-id"
            pagination="${pagination}"
            numElements="${projects.size()}"
    />
</content>
