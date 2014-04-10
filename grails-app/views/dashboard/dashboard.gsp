<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
</head>


<content tag="mainContent">
    <!-- COMIENZA LISTA DE KAKAREOS Y SEGUIMIENTOS -->
    <ul id="dashboard-kakareos-id" class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">
        <g:render template="liClucks" model="[clucks:clucks]"/>
    </ul>
    <!-- ver más -->
    <div id="load-more" class="text-center">
        <g:link mapping="dashboardSeeMore" class="loadMore" data-parent-id="dashboard-kakareos-id">
            <g:message code="dashboard.clucks.seeMore"/>
        </g:link>
    </div>

    <g:each in="${clucks}" var="cluck">
        <div>
            <div>
                <g:if test="${user.favorites.contains(cluck.post.id)}">
                    <g:link mapping="postDelFavorite" params="${cluck.post.encodeAsLinkProperties()}">Ya leido</g:link>
                </g:if>
                <g:else>
                    <g:link mapping="postAddFavorite" params="${cluck.post.encodeAsLinkProperties()}">Leer despues</g:link>
                </g:else>
            </div>
        </div>
    </g:each>
</content>

<content tag="cColumn">
    <g:include controller="modules" action="userProfile"/>
    <g:include controller="modules" action="userProfileAlerts"/>
    <g:include controller="modules" action="recommendedPosts"/>
    <g:include controller="modules" action="userFavorites"/>
</content>
