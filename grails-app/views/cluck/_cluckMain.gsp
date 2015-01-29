
<h1>${post.title} <g:link mapping="projectShow" itemprop="keywords" params="${post.project.encodeAsLinkProperties()}">${post.project.hashtag}</g:link></h1>

<postUtil:ifHasMultimedia post="${post}">
    <postUtil:postShowMultimedia post="${post}"/>
</postUtil:ifHasMultimedia>

<g:render template="/cluck/cluckUsers" model="[post:post]"/>