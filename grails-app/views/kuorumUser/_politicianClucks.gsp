<div id="projectChangePostTypeButtonDiv" class="btn-group btn-group-justified filters" role="group" aria-label="Justified button group">
    <g:if test="${numUserProjects}">
        <a href="#" class="btn active" role="button" data-showDivId="clucks">
            <g:message code="kuorumUser.show.politicianList.projects.title"/>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'kuorumUser.show.politicianList.projects.title')}" role="log" class="badge">${numUserProjects}</span>
        </a>
    </g:if>
    <g:if test="${numDefendedPosts}">
        <a href="#" class="btn" role="button" data-showDivId="defends">
            <g:message code="kuorumUser.show.politicianList.defendedPosts.title"/>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'kuorumUser.show.politicianList.defendedPosts.title')}" role="log" class="badge">${numDefendedPosts}</span>
        </a>
    </g:if>
    <g:if test="${numUserVictoryPosts}">
        <a href="#" class="btn" role="button" data-showDivId="victories">
            <g:message code="kuorumUser.show.politicianList.victoryPosts.title"/>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'kuorumUser.show.politicianList.victoryPosts.title')}" role="log" class="badge">${numUserVictoryPosts}</span>
        </a>
    </g:if>
</div>
<!-- COMIENZA LISTA DE KAKAREOS -->
<g:set var="urlLoadMoreProjects" value="${g.createLink(mapping: 'ajaxPoliticianProjects', params:user.encodeAsLinkProperties())}"/>
<div id="clucks" data-name="listClucks" class="row">
    <g:render template="/project/listProjects" model="[projects:userProjects, urlLoadMore:urlLoadMoreProjects, seeMore:userProjects.size()< numUserProjects]"/>
</div>
<g:set var="urlLoadMorePosts" value="${g.createLink(mapping: 'ajaxPoliticianDefendedPosts', params:user.encodeAsLinkProperties())}"/>
<div id="defends" class="hidden" data-name="listClucks">
    <g:render template="/cluck/listPosts" model="[posts:defendedPosts, urlLoadMore:urlLoadMorePosts, seeMore:defendedPosts.size()<numDefendedPosts]"/>
</div>
<g:set var="urlLoadMoreVictories" value="${g.createLink(mapping: 'ajaxPoliticianVictoryPosts', params:user.encodeAsLinkProperties())}"/>
<div id="victories" class="hidden" data-name="listClucks">
    <g:render template="/cluck/listPosts" model="[posts:userVictoryPosts, urlLoadMore:urlLoadMoreVictories, seeMore:userVictoryPosts.size()<numUserVictoryPosts]"/>
</div>
