<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${post.title}</title>
    <meta name="layout" content="columnCFloatingLayout">
    <g:render template="postMetaTags" model="[post:post]"/>
</head>


<content tag="mainContent">
    <article class="kakareo post" itemscope itemtype="http://schema.org/Article" role="article" data-cluck-postId="${post.id}">
        <g:render template="/cluck/cluckMenuEditPost" model="[post:post]"/>

        <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
        <h1>${post.title} <g:link mapping="projectShow" params="${post.project.encodeAsLinkProperties()}">${post.project.hashtag}</g:link> </h1>
        <div class="main-kakareo row">
            <div class="col-xs-5 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <userUtil:showUser user="${post.owner}" showRole="true"/>
            </div>
            <div class="col-xs-7 text-right sponsor">
                <userUtil:showDebateUsers post="${post}" visibleUsers="1"/>
            </div>
        </div>
        <postUtil:postShowMultimedia post="${post}"/>
        <g:render template="/cluck/footerCluck" model="[post:post, displayingColumnC:false]"/>
        <p>
            ${raw(post.text.encodeAsRemovingScriptTags().replaceAll('<br>','</p><p>'))}
        </p>
    </article>
    <g:render template="/post/debates/postDebates" model="[post:post]"/>
    <g:render template="postComments" model="[post:post]"/>
    <g:render template="relatedPosts" model="[relatedPosts:relatedPost]"/>

</content>

<content tag="cColumn">
    <section class="boxes vote drive">
        <g:render template="likesContainer" model="[post:post]"/>
        <div class="sponsor">
            <userUtil:showListUsers users="${usersVotes}" visibleUsers="5" messagesPrefix="post.show.boxes.like.userList"/>
        </div>
        <g:render template="/post/postVotePostButton" model="[post:post, userVote:userVote]"/>

        <g:render template="/post/postSocialShare" model="[post:post]"/>
    </section>
</content>

%{--<content tag="footerStats">--}%

    %{--<modulesUtil:delayedModule mapping="ajaxModuleProjectBottomStats" params="[hashtag:project.hashtag.decodeHashtag()]" elementId="idAjaxModuleProjectBottomStats"/>--}%
    %{--<g:include controller="modules" action="bottomProjectStats" params="[project:project]"/>--}%
    %{--<a href="#main" class="smooth top">--}%
        %{--<span class="fa fa-caret-up fa-lg"></span>--}%
        %{--<g:message code="project.up"/>--}%
    %{--</a>--}%
%{--</content>--}%