<g:set var="multimedia" value=""/>
<postUtil:ifHasMultimedia post="${post}">
    <g:set var="multimedia" value="multimedia"/>
</postUtil:ifHasMultimedia>

<div class="author ${important}">
    <postUtil:politiciansHeadPost post="${post}"/>
    <article class="kakareo post ${multimedia}" role="article" itemscope itemtype="http://schema.org/Article" data-cluck-postId="${post.id}">
        <div class="wrapper">
            <g:render template="/cluck/cluckMain" model="[post:post]"/>
        </div>
        <g:render template="/cluck/footerCluck" model="[cluck:post, displayingColumnC:false]"/>

        <g:render template="/post/postBody" model="[post:post]"/>
        <g:render template="/post/debates/postDebates" model="[post:post]"/>
        <div class="wrapper">
            <g:render template="/cluck/cluckUsers" model="[post:post]"/>
        </div>
        <g:render template="/cluck/footerCluck" model="[post:post,displayingColumnC:false]"/>
    </article><!-- /article -->
</div>