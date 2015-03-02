<div id="projectChangePostTypeButtonDiv" class="btn-group btn-group-justified filters" role="group" aria-label="Justified button group">
    <g:if test="${numClucks}">
        <a href="#" class="btn active" role="button" data-showDivId="clucks">
            <g:message code="kuorumUser.show.userList.clucks.title"/>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'kuorumUser.show.userList.clucks.title')}" role="log" class="badge">${numClucks}</span>
        </a>
    </g:if>
    <g:if test="${numUserPost}">
        <a href="#" class="btn" role="button" data-showDivId="defends">
            <g:message code="kuorumUser.show.userList.post.title"/>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'kuorumUser.show.userList.post.title')}" role="log" class="badge">${numUserPost}</span>
        </a>
    </g:if>
    <g:if test="${numUserVictoryPosts}">
        <a href="#" class="btn" role="button" data-showDivId="victories">
            <g:message code="kuorumUser.show.userList.victories.title"/>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'kuorumUser.show.userList.victories.title')}" role="log" class="badge">${numUserVictoryPosts}</span>
        </a>
    </g:if>
</div>
<!-- COMIENZA LISTA DE KAKAREOS -->
<g:set var="urlLoadMoreClucks" value="${g.createLink(mapping: 'userClucks', params:user.encodeAsLinkProperties())}"/>
<div id="clucks" data-name="listClucks">
    <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMoreClucks, seeMore:clucks.size()< numClucks]"/>
</div>
<g:set var="urlLoadMorePosts" value="${g.createLink(mapping: 'userPost', params:user.encodeAsLinkProperties())}"/>
<div id="defends" class="hidden" data-name="listClucks">
    <g:render template="/cluck/listPosts" model="[posts:userPosts, urlLoadMore:urlLoadMorePosts, seeMore:userPosts.size()<numUserPost]"/>
</div>
<g:set var="urlLoadMoreVictories" value="${g.createLink(mapping: 'userVictories', params:user.encodeAsLinkProperties())}"/>
<div id="victories" class="hidden" data-name="listClucks">
    <g:render template="/cluck/listPosts" model="[posts:userVictoryPosts, urlLoadMore:urlLoadMoreVictories, seeMore:userVictoryPosts.size()<numUserVictoryPosts]"/>
</div>
