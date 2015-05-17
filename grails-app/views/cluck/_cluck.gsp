
<g:set var="post" value="${post!=null?post:cluck.post}"/>
<g:set var="displayingColumnC" value="${displayingColumnC?:false}"/>
<g:set var="displayingHorizontalModule" value="${displayingHorizontalModule?:false}"/>

<g:set var="classWidthUserModules" value="${displayingHorizontalModule?'col-sm-12':'col-sm-6'}"/>

<article class="kakareo" itemscope itemtype="http://schema.org/Article" role="article" data-cluck-postId="${post.id}">
    <g:render template="/cluck/cluckMenuEditPost" model="[post:post]"/>

    <div class="link-wrapper">
    <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
    <g:if test="${cluck}">
        <div class="user actor" itemscope itemtype="http://schema.org/Person">
            <userUtil:showUser user="${cluck.owner}" showRole="false"/>
            <span class="action"><g:message code="cluck.header.action.${cluck.cluckAction}"/> <kuorumDate:humanDate date="${cluck.dateCreated}"/></span>
        </div>
    </g:if>

        <h1>${post.title} <g:link mapping="projectShow" params="${post.project.encodeAsLinkProperties()}">${post.project.hashtag}</g:link> </h1>
        <g:render template="/post/postUsers" model="[post:post, displayingHorizontalModule:displayingHorizontalModule]"/>
        <g:if test="${!displayingColumnC}">
            <g:render template="/cluck/cluckMultimedia" model="[post:post]"/>
        </g:if>
        <g:render template="/cluck/footerCluck" model="[post:post, displayingColumnC:displayingColumnC]"/>
    </div>
</article>