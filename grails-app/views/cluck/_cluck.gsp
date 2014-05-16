<g:set var="important" value="${false}"/>
<postUtil:ifIsImportant post="${cluck.post}">
    <g:set var="important" value="${true}"/>
</postUtil:ifIsImportant>
<g:set var="recluck" value="${cluck.post.owner!=cluck.owner}"/>

<g:set var="liCss" value="author"/>
<postUtil:ifHasMultimedia post="${cluck.post}">
    <g:set var="liCss" value="author multimedia"/>
</postUtil:ifHasMultimedia>

<li class="${liCss} ${important?'important':''}" itemscope itemtype="http://schema.org/Article">
    <g:if test="${recluck}">
        <g:render template="/cluck/reCluckUser" model="[cluck:cluck]"/>
    </g:if>

    <postUtil:politiciansHeadPost cluck="${cluck}"/>

    <article class="kakareo" role="article" data-cluck-postId="${cluck.post.id}">
        <g:if test="${recluck && !important}">
            <span class="from"><span class="inside"></span></span>
        </g:if>
        <div class="link-wrapper">
            <g:link mapping="postShow" params="${cluck.post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
            <g:render template="/cluck/cluckMain" model="[post: cluck.post]"/>
        </div>
        <g:render template="/cluck/footerCluck" model="[post:cluck.post,displayingColumnC:false]"/>
    </article><!-- /article -->

</li><!-- /kakareo-->
