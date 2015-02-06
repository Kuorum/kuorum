<li>
    <article class="kakareo" itemscope itemtype="http://schema.org/Article" role="article" data-cluck-postId="${cluck.post.id}">
        <g:render template="/cluck/cluckMenuEditPost" model="[cluck:cluck]"/>

        <div class="link-wrapper">
        <g:link mapping="postShow" params="${cluck.post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
            <h1>${cluck.post.title} <g:link mapping="projectShow" params="${cluck.project.encodeAsLinkProperties()}">${cluck.project.hashtag}</g:link> </h1>
            <div class="main-kakareo row">
                <div class="col-md-5 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                    <userUtil:showUser user="${cluck.post.owner}" showRole="true"/>
                </div>
                <div class="col-md-7 text-right sponsor">
                    <userUtil:showDebateUsers post="${cluck.post}" visibleUsers="1"/>
                </div>
            </div>

            <g:render template="/cluck/footerCluck" model="[post:cluck.post, displayingColumnC:false]"/>
        </div>
    </article>
</li>