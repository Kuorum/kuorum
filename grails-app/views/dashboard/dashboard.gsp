
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file: 'home1.jpg')}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <parameter name="hideFooter" value="true"/>
</head>


<content tag="mainContent">

    <sec:ifLoggedIn>
        <showNoticesDataIncomplete:showWarningsDataProfileIncomplete />
    </sec:ifLoggedIn>

    <g:if test="${clucks}">
        <g:set var="urlLoadMore" value="${createLink(mapping: 'dashboardSeeMore')}"/>
        <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore, seeMore:seeMore]"/>
    </g:if>
    %{--<g:else>--}%
        %{--<g:render template="emptyClucks" model="[mostActiveUsers:mostActiveUsers]"/>--}%
    %{--</g:else>--}%
</content>

<content tag="cColumn">
    <g:include controller="modules" action="userProfile"/>
    <g:include controller="modules" action="userProfileAlerts"/>
    <g:include controller="modules" action="recommendedUsers"/>
    <modulesUtil:recommendedPosts title="${message(code:"modules.recommendedPosts.title")}"/>
    <g:include controller="modules" action="userFavorites"/>
</content>

%{--<content tag="footerStats">--}%
    %{--<a href="#main" class="smooth top">--}%
        %{--<span class="fa fa-caret-up fa-lg"></span>--}%
        %{--<g:message code="project.up"/>--}%
    %{--</a>--}%
%{--</content>--}%
