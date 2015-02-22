
<g:set var="post" value="${post!=null?post:cluck.post}"/>
<article class="kakareo" itemscope itemtype="http://schema.org/Article" role="article" data-cluck-postId="${post.id}">
    <g:render template="/cluck/cluckMenuEditPost" model="[post:post]"/>

    <div class="link-wrapper">
    <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
        <h1>${post.title} <g:link mapping="projectShow" params="${post.project.encodeAsLinkProperties()}">${post.project.hashtag}</g:link> </h1>
        <div class="main-kakareo row">
            <div class="col-md-6 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <userUtil:showUser user="${post.owner}" showRole="true"/>
            </div>
            <div class="col-md-6 text-right sponsor">
                <userUtil:showDebateUsers post="${post}" visibleUsers="1"/>
            </div>
        </div>

        <g:render template="/cluck/footerCluck" model="[post:post, displayingColumnC:false]"/>
    </div>
</article>