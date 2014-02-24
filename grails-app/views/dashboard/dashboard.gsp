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
            <h3><h2>${cluck.postOwner}</h2></h3>
            <h2>${cluck.post.title}</h2>
        </div>
    </g:each>
</content>

<content tag="cColumn">
    Columna C de dashboard
</content>
