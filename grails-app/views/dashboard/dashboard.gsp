<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
</head>


<content tag="mainContent">
    DASHBOARD

    <H1> Kakareos</H1>

    <g:each in="${clucks}" var="cluck">
        <div>
            <h3>CLUKER: ${cluck.owner.name} || postOwner: ${cluck.postOwner} </h3>
            <h4><g:link mapping="lawShow" params="${cluck.law.encodeAsLinkProperties()}">${cluck.law.hashtag}</g:link> </h4>
            <h2>${cluck.post.title}</h2><span><g:link mapping="postShow" params="${cluck.post.encodeAsLinkProperties()}">VER POST</g:link> </span>
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
