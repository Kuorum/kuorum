<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="kuorumLayout">
</head>


<content tag="mainContent">
    DASHBOARD

    <H1> Kakareos</H1>

    <g:each in="${clucks}" var="cluck">
        <div>
            <h3>${cluck.postOwner}</h3>
            <h4><g:link mapping="lawShow" params="${[hashtag:cluck.law.hashtag[1..-1]]}">${cluck.law.hashtag}</g:link> </h4>
            <h2>${cluck.post.title}</h2>
            <div> <g:link mapping="postCluck" params="[postId:cluck.post.id]">Cluck IT</g:link> </div>
        </div>
    </g:each>
</content>

<content tag="cColumn">
    Columna C de dashboard
</content>
