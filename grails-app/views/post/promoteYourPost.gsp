<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="kuorumLayout">
</head>


<content tag="mainContent">
    <H1> Impulsta tu ${post.postType}</H1>
    <H3> ${post.title}</H3>

    <kPost:postLink post="${post}">Ver tu ${post.postType}</kPost:postLink>
</content>

<content tag="cColumn">
    Columna C de dashboard
</content>
