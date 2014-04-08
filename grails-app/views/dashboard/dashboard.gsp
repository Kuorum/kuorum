<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
</head>


<content tag="mainContent">
    <!-- COMIENZA LISTA DE KAKAREOS Y SEGUIMIENTOS -->
    <ul class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">
        <g:each in="${clucks}" var="cluck">
            <g:render template="/cluck/cluck" model="[cluck:cluck]"/>
        </g:each>
    </ul>
    <H1> Kakareos</H1>

    <g:each in="${clucks}" var="cluck">
        <div>
            <div> <g:link mapping="cluckCreate" params="[postId:cluck.post.id]">Cluck IT</g:link> </div>
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
    Columna C de dashboard
    <g:include controller="modules" action="userProfile"/>
    <g:include controller="modules" action="userProfileAlerts"/>
    <g:include controller="modules" action="recommendedPosts"/>
</content>
