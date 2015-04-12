
<g:set var="post" value="${post!=null?post:cluck.post}"/>
<g:set var="displayingColumnC" value="${displayingColumnC?:false}"/>
<g:set var="displayingHorizontalModule" value="${displayingHorizontalModule?:false}"/>

<g:set var="classWidthUserModules" value="${displayingHorizontalModule?'col-sm-12':'col-sm-6'}"/>

<article class="kakareo" itemscope itemtype="http://schema.org/Article" role="article" data-cluck-postId="${post.id}">
    <g:render template="/cluck/cluckMenuEditPost" model="[post:post]"/>

    <div class="link-wrapper">
    <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
        <h1>${post.title} <g:link mapping="projectShow" params="${post.project.encodeAsLinkProperties()}">${post.project.hashtag}</g:link> </h1>
        <div class="main-kakareo row">
            <div class="${classWidthUserModules} user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <userUtil:showUser user="${post.owner}" showRole="true"/>
            </div>
            <g:if test="${post.defender}">
                <div class="${classWidthUserModules} text-right sponsor">
                    <userUtil:showListUsers users="${[post.defender]}" visibleUsers="1" messagesPrefix="cluck.defendUsers"/>
                </div>
            </g:if>
            <g:else>
                <div class="${classWidthUserModules} text-right sponsor">
                    <userUtil:showDebateUsers post="${post}" visibleUsers="1"/>
                </div>
            </g:else>
        </div>
        <g:if test="${!displayingColumnC}">
            <g:render template="/cluck/cluckMultimedia" model="[post:post]"/>
        </g:if>
        <g:render template="/cluck/footerCluck" model="[post:post, displayingColumnC:displayingColumnC]"/>
    </div>
</article>