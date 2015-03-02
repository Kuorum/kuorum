<h2 class="underline"><g:message code="project.clucks.title"/> </h2>

<div id="projectChangePostTypeButtonDiv" class="btn-group btn-group-justified filters" role="group" aria-label="Justified button group">
    <g:if test="${numClucks}">
        <a href="#" class="btn active" role="button" data-showDivId="clucks">
            <g:message code="project.clucks.newer"/>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'project.clucks.newer')}" role="log" class="badge">${numClucks}</span>
        </a>
    </g:if>
    <g:if test="${numDefends}">
        <a href="#" class="btn" role="button" data-showDivId="defends">
            <g:message code="project.clucks.defended"/>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'project.clucks.defended')}" role="log" class="badge">${numDefends}</span>
        </a>
    </g:if>
    <g:if test="${numVictories}">
        <a href="#" class="btn" role="button" data-showDivId="victories">
            <g:message code="project.clucks.vicotries"/>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'project.clucks.vicotries')}" role="log" class="badge">${numVictories}</span>
        </a>
    </g:if>
</div>
<!-- COMIENZA LISTA DE KAKAREOS -->
<g:set var="urlLoadMorePosts" value="${g.createLink(mapping:'projectListClucks', params: project.encodeAsLinkProperties())}"/>
<div id="clucks" data-name="listClucks">
    <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMorePosts, seeMore:seeMoreClucks]"/>
</div>
<g:set var="urlLoadMoreDefends" value="${g.createLink(mapping:'projectListPostDefends', params: project.encodeAsLinkProperties())}"/>
<div id="defends" class="hidden" data-name="listClucks">
    <g:render template="/cluck/listPosts" model="[posts:defends, urlLoadMore:urlLoadMoreDefends, seeMore:seeMoreDefends]"/>
</div>
<g:set var="urlLoadMoreVictories" value="${g.createLink(mapping:'projectListPostVictories', params: project.encodeAsLinkProperties())}"/>
<div id="victories" class="hidden" data-name="listClucks">
    <g:render template="/cluck/listPosts" model="[posts:victories, urlLoadMore:urlLoadMoreVictories, seeMore:seeMoreVictories]"/>
</div>
