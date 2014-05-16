
<g:set var="important" value="${false}"/>
<postUtil:ifIsImportant post="${post}">
    <g:set var="important" value="${true}"/>
</postUtil:ifIsImportant>

<li class="author ${important?'important':''}" itemscope itemtype="http://schema.org/Article">
    <g:render template="/modules/columnCPost/columnCPostPoliticians" model="[post:post]"/>
    <article class="kakareo" role="article" data-cluck-postId="${post.id}">
        <div class="link-wrapper">
            <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
            <h1>${post.title} <g:link mapping="lawShow" params="${post.law.encodeAsLinkProperties()}">${post.law.hashtag}</g:link></h1>
            <div class="main-kakareo row">
                <!-- cambia la clase col-md era 5 y pasa a ser 6 -->
                <div class="col-md-6 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                    <userUtil:showUser user="${post.owner}"/>
                </div><!-- /autor -->

            <!-- cambia la clase col-md era 7 y pasa a ser 6 -->
                <div class="col-md-6 text-right sponsor">
                    <userUtil:showListUsers users="${post.sponsors.kuorumUser}" visibleUsers="2" messagesPrefix="post.promoters.userList"/>
                </div><!-- /patrocinadores -->
            </div>
        </div><!-- /.link-wrapper -->
        <g:render template="/cluck/footerCluck" model="[post:post, displayingColumnC:true]"/>
    </article><!-- /article -->
</li><!-- /kakareo important -->