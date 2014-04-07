<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.showFavoritesPosts"/> </title>
    <meta name="layout" content="normalLayout">
</head>


<content tag="mainContent">
    <H1> Pendientes de leer:  ${user.name}</H1>

    <ul>
    <g:each in="${favorites}" var="favoritePost">
        <li>
            Law:<g:link mapping="lawShow" params="${favoritePost.law.encodeAsLinkProperties()}">${favoritePost.law.hashtag}</g:link>
            <br/>
            Title: <g:link mapping="postShow" params="${favoritePost.encodeAsLinkProperties()}">${favoritePost.title}</g:link>
            <hr/>
        </li>

    </g:each>
    </ul>
</content>
