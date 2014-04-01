<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
</head>


<content tag="mainContent">
    <H1> Impulsta tu ${post.postType}</H1>
    <H3> ${post.title}</H3>

    <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}">Ver tu ${post.postType}</g:link>
</content>

<content tag="cColumn">
    Columna C de promocionando
</content>
