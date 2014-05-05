<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
</head>


<content tag="mainContent">
    <g:set var="urlLoadMore" value="${createLink(mapping: 'dashboardSeeMore')}"/>
    <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore]"/>
</content>

<content tag="cColumn">
    <g:include controller="modules" action="userProfile"/>
    <g:include controller="modules" action="userProfileAlerts"/>
    <g:include controller="modules" action="recommendedPosts"/>
    <g:include controller="modules" action="userFavorites"/>
</content>
