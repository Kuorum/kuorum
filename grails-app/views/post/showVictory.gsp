<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${post.title}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="postMetaTags" model="[post:post]"/>
</head>


<content tag="mainContent">
    <g:set var="important" value=""/>
    <postUtil:ifIsImportant post="${post}">
        <g:set var="important" value="important"/>
    </postUtil:ifIsImportant>

    <g:render template="postHeadCluck" model="[post:post, important: important]"/>
    <g:render template="relatedPosts" model="[relatedPosts:relatedPost]"/>
    <g:render template="postComments" model="[post:post]"/>

</content>

<content tag="cColumn">

<section class="boxes noted likes important">
<img class="actor" src="${image.userImgSrc(user:post.owner)}" alt="${post.owner.name}">
<h1><g:message code="post.victory.title" args="[post.owner.name]"/> </h1>
<p class="text-left"><g:message code="post.victory.subTitle"/> </p>
    <g:render template="likesContainer" model="[post:post]"/>
    <div class="sponsor">
        <userUtil:showListUsers users="${usersVotes}" visibleUsers="5" messagesPrefix="post.show.boxes.like.userList"/>
    </div>
    <g:render template="/post/postVotePostButton" model="[post:post, userVote:userVote]"/>
    <g:render template="/post/postSocialShare" model="[post:post]"/>

</section>
</content>

<content tag="footerStats">

    %{--<modulesUtil:delayedModule mapping="ajaxModuleProjectBottomStats" params="[hashtag:project.hashtag.decodeHashtag()]" elementId="idAjaxModuleProjectBottomStats"/>--}%
    %{--<g:include controller="modules" action="bottomProjectStats" params="[project:project]"/>--}%
    <a href="#main" class="smooth top">
        <span class="fa fa-caret-up fa-lg"></span>
        <g:message code="project.up"/>
    </a>
</content>