
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file: 'home1.jpg')}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <g:if test="${tour}">
        <r:require module="tour"/>
    </g:if>
</head>


<content tag="mainContent">
    <h2 class="underline">Suggested causes for you</h2>
    <ul class="causes-list row" id="cause-card-list-id">
        <g:render template="/dashboard/dashboardModules/causeCardList" model="[causes:causesSuggested.data]"/>
    </ul>

    <!-- ver más -->
    <nav:loadMoreLink
            mapping="dashboardCausesSeeMore"
            parentId="cause-card-list-id"
            pagination="${causesPagination}"
            numElements="${causesSuggested.total}"/>


    <h2 class="underline">Politicians who recently joined</h2>
    <!-- LISTA DE POLÍTICOS -->
    <ul class="politician-list row" id="search-list-id">
        <g:render template="/search/searchElement" model="[docs:politicians.elements, columnsCss:'col-sm-6']"/>
    </ul>

    <nav:loadMoreLink
            formId="search-form-loadMore"
            mapping="searcherSearchSeeMore"
            parentId="search-list-id"
            pagination="${searchPoliticiansPagination}"
            numElements="${politicians.numResults}"
    >
        <input type="hidden" name="word" value="${searchPoliticiansPagination.word}" />
        <input type="hidden" name="type" value="${searchPoliticiansPagination.type}" />
    </nav:loadMoreLink>
</content>

<content tag="cColumn">
    <g:include controller="modules" action="userProfile"/>
    <g:render template="/dashboard/dashboardModules/ipdbRecruitmentCard" model="[user:loggedUser]"/>
    %{--<g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/leaningIndex" model=""/>--}%
    %{--<g:render template="dashboardModules/supportedCauses" model="[user:loggedUser, supportedCauses:supportedCauses]"/>--}%
    <nav:delayedSection divId="user-leainingIndex-wrapper-id" mapping="ajaxModuleUserLeaningIndex" params="${[]}"/>
    <nav:delayedSection divId="user-causes-wrapper-id" mapping="ajaxModuleUserCauses" params="${[]}" reload="true"/>
    <g:include controller="modules" action="recommendedUsers"/>
    <g:include controller="modules" action="recommendedPoliticiansUserDashboard"/>
</content>
