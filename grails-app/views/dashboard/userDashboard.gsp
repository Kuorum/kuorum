
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file: 'home1.jpg')}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
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
    <ul class="politician-list row">
        <g:render template="/search/searchElement" model="[docs:politicians, columnsCss:'col-sm-6']"/>
    </ul>
</content>

<content tag="cColumn">
    <g:include controller="modules" action="userProfile"/>
    <g:render template="dashboardModules/ipdbRequitmentCard"/>
    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/leaningIndex" model="[user:loggedUser, leaningIndex: userLeaningIndex]"/>
    <g:render template="dashboardModules/supportedCauses" model="[user:loggedUser, supportedCauses:supportedCauses]"/>
    <g:include controller="modules" action="recommendedUsers"/>
    <g:include controller="modules" action="recommendedPoliticiansUserDashboard"/>
</content>
