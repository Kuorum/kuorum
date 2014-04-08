<g:set var="important" value="${!cluck.post.debates.isEmpty() || cluck.supportedBy}"/>
<g:set var="recluck" value="${cluck.post.owner!=cluck.owner}"/>

<li class="author ${important?'important':''}" itemscope itemtype="http://schema.org/Article">
    <g:if test="${recluck}">
        <g:render template="/cluck/reCluckUser" model="[cluck:cluck]"/>
    </g:if>

    <g:if test="${important}">
        <g:render template="/cluck/politicianPosts" model="[recluck:(recluck && important)]"/>
    </g:if>

    <article class="kakareo" role="article">
        <g:link mapping="postShow" params="${cluck.post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
        <g:if test="${recluck && !important}">
            <span class="from"><span class="inside"></span></span>
        </g:if>
        <div class="link-wrapper">
            <h1>${cluck.post.title}<g:link mapping="lawShow" params="${cluck.law.encodeAsLinkProperties()}">${cluck.law.hashtag}</g:link></h1>
            <div class="main-kakareo row">
                <g:render template="/cluck/cluckOwnerPost" model="[user:cluck.postOwner]"/>

                <g:render template="/cluck/cluckSponsorPost"/>
            </div>
        </div>
        <g:render template="/cluck/footerCluck" model="[cluck:cluck]"/>
    </article><!-- /article -->

</li><!-- /kakareo-->
