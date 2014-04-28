
<h1>${post.title} <g:link mapping="lawShow" itemprop="keywords" params="${post.law.encodeAsLinkProperties()}">${post.law.hashtag}</g:link></h1>

<postUtil:ifHasMultimedia post="${post}">
    <postUtil:postShowImage post="${post}"/>
</postUtil:ifHasMultimedia>

<g:render template="/cluck/cluckUsers" model="[post:post]"/>