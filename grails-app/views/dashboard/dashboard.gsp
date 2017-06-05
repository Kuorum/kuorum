
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <!-- Schema.org markup for Google+ -->
    <g:if test="${tour}">
        <r:require module="tour"/>
    </g:if>
</head>


<content tag="mainContent">
    <h2 class="underline"><g:message code="dashboard.causes.title"/> </h2>
    <ul class="causes-list row" id="cause-card-list-id">
        <g:render template="/dashboard/dashboardModules/causeCardList" model="[causes:causesSuggested.data]"/>
    </ul>

    <!-- ver más -->
    <nav:loadMoreLink
            formId="cause-card-list-loadMore"
            mapping="dashboardCausesSeeMore"
            parentId="cause-card-list-id"
            pagination="${causesPagination}"
            numElements="${causesSuggested.total}"/>


    <h2 class="underline"><g:message code="dashboard.newPoliticians.title"/> </h2>
    <!-- LISTA DE POLÍTICOS -->
    <ul class="politician-list row" id="search-list-id">
        <g:render template="/dashboard/listDashboardUserRecommendations" model="[politicians:politicians]"/>
    </ul>

    <nav:loadMoreLink
            formId="search-form-loadMore"
            mapping="dashboardPoliticiansSeeMore"
            parentId="search-list-id"
            pagination="${politiciansDashboardPagination}"
            numElements="${2000}"
    />

</content>

<content tag="cColumn">
    <g:include controller="modules" action="userProfile"/>
    <g:render template="/dashboard/dashboardModules/ipdbRecruitmentCard" model="[user:loggedUser]"/>
    %{--<g:render template="dashboardModules/supportedCauses" model="[user:loggedUser, supportedCauses:supportedCauses]"/>--}%
    <nav:delayedSection divId="user-causes-wrapper-id" mapping="ajaxModuleUserCauses" params="${[]}"/>
    <g:include controller="modules" action="recommendedPoliticiansUserDashboard"/>
    <g:include controller="modules" action="recommendedUsers"/>
    <g:render template="/dashboard/dashboardModules/shareSocialNetworks" model="[user:loggedUser]"/>
</content>
