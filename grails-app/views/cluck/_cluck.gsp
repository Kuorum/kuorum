<g:set var="important" value="${false}"/>
<postUtil:ifIsImportant post="${cluck.post}">
    <g:set var="important" value="${true}"/>
</postUtil:ifIsImportant>
<g:set var="recluck" value="${cluck.post.owner!=cluck.owner}"/>

<g:set var="liCss" value="author"/>
<postUtil:ifHasMultimedia post="${cluck.post}">
    <g:set var="liCss" value="multimedia"/>
</postUtil:ifHasMultimedia>

<li class="${liCss} ${important?'important':''}" itemscope itemtype="http://schema.org/Article">
    <g:if test="${recluck}">
        <g:render template="/cluck/reCluckUser" model="[cluck:cluck]"/>
    </g:if>

    <g:if test="${important}">
        <g:render template="/cluck/politicianPosts" model="[recluck:(recluck && important)]"/>
    </g:if>

    <article class="kakareo" role="article" data-cluck-postId="${cluck.post.id}">
        <g:if test="${recluck && !important}">
            <span class="from"><span class="inside"></span></span>
        </g:if>
        <div class="link-wrapper">
            <g:link mapping="postShow" params="${cluck.post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
            <h1>${cluck.post.title} <g:link mapping="lawShow" params="${cluck.law.encodeAsLinkProperties()}">${cluck.law.hashtag}</g:link></h1>

            <postUtil:ifHasMultimedia post="${cluck.post}">
                %{--<g:render template="/cluck/cluckPhoto"/>--}%
                <g:render template="/cluck/cluckVideo"/>
            </postUtil:ifHasMultimedia>
            <div class="main-kakareo row">
                <g:render template="/cluck/cluckOwnerPost" model="[user:cluck.postOwner]"/>

                <g:render template="/cluck/cluckSponsorPost"/>
            </div>
        </div>
        <g:render template="/cluck/footerCluck" model="[post:cluck.post]"/>
    </article><!-- /article -->

</li><!-- /kakareo-->
