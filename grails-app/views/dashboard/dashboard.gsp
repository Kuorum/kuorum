
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.dashboard"/> </title>
    <meta name="layout" content="columnCLayout">
</head>


<content tag="mainContent">
    <g:if test="${clucks}">
        <g:set var="urlLoadMore" value="${createLink(mapping: 'dashboardSeeMore')}"/>
        <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore, seeMore:seeMore]"/>
    </g:if>
    <g:else>
        <g:render template="emptyClucks" model="[mostActiveUsers:mostActiveUsers]"/>
    </g:else>
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
        %{--<g:message code="law.up"/>--}%
    %{--</a>--}%
%{--</content>--}%
