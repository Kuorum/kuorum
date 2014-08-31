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
    <p><g:message code="discover.menu.recommendedPosts.description"/></p>
    <g:render template="discoverLeftMenu" model="[activeMapping:'discoverRecommendedPosts']"/>

</content>

<content tag="mainContent">
    <div class="clearfix">
        <h1>
            <g:message code="discover.title.discover.recommendedPosts"/>
        </h1>
    </div>
    <ul id="politician-list-id">
        <g:render template="discoverPostsList" model="[posts:posts]"/>
    </ul>

    <nav:loadMoreLink
            mapping="discoverRecommendedPosts"
            parentId="politician-list-id"
            pagination="${pagination}"
            numElements="${posts.size()}"
    />
</content>
