<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.discover.laws"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <parameter name="idMainContent" value="search-results" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="discover.menu.discover"/>
    </h1>
    %{--<p><g:message code="discover.menu.laws.description"/></p>--}%
    <g:render template="discoverLeftMenu" model="[activeMapping:'discoverLaws']"/>

</content>

<content tag="mainContent">
    <div class="clearfix">
        <h1>
            <g:message code="discover.menu.discover.laws"/>
        </h1>
    </div>
    <ul id="law-list-id">
        <g:render template="discoverLawList" model="[laws:laws]"/>
    </ul>

    <nav:loadMoreLink
            mapping="discoverLaws"
            parentId="law-list-id"
            pagination="${pagination}"
            numElements="${laws.size()}"
    />
</content>
