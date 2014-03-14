<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="kuorumLayout">
</head>


<content tag="mainContent">
    REVIEW

    <H1> Review del post</H1>
    <H3> ${post.title}</H3>

    <g:link mapping="postPublish" params="[postId:post.id, postTypeUrl:post.postType.urlText, hashtag:post.law.hashtag.decodeHashtag()]">PUBLICAR</g:link>
</content>

<content tag="cColumn">
    Columna C de dashboard
</content>
