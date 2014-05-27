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

    <g:set var="multimedia" value=""/>
    <postUtil:ifHasMultimedia post="${post}">
        <g:set var="multimedia" value="multimedia"/>
    </postUtil:ifHasMultimedia>

    <div class="author ${important}">
        <postUtil:politiciansHeadPost post="${post}"/>
        <article class="kakareo post ${multimedia}" role="article" itemscope itemtype="http://schema.org/Article" data-cluck-postId="${post.id}">
            <div class="wrapper">
                <g:render template="/cluck/cluckMain" model="[post:post]"/>
            </div>
            <g:render template="/cluck/footerCluck" model="[cluck:post, displayingColumnC:false]"/>

            <g:render template="postBody" model="[post:post]"/>
            <g:render template="debates/postDebates" model="[post:post]"/>
            <div class="wrapper">
                <g:render template="/cluck/cluckUsers" model="[post:post]"/>
            </div>
            <g:render template="/cluck/footerCluck" model="[post:post,displayingColumnC:false]"/>
        </article><!-- /article -->

        <g:render template="relatedPosts" model="[relatedPosts:relatedPost]"/>
        <g:render template="postComments" model="[post:post]"/>
    </div>
</content>

<content tag="cColumn">
    <section class="boxes noted likes ${important}">
        <h1><g:message code="post.show.boxes.like.title"/></h1>
        <p class="text-left"><g:message code="post.show.boxes.like.description"/> </p>
        <g:render template="likesContainer" model="[post:post]"/>
        <div class="sponsor">
            <userUtil:showListUsers users="${usersVotes}" visibleUsers="5" messagesPrefix="post.show.boxes.like.userList"/>
        </div>
        <g:render template="/post/postVotePostButton" model="[post:post, userVote:userVote, important:important]"/>

        <g:render template="postSocialShare" model="[post:post]"/>

    </section>


</content>

<content tag="footerStats">

    %{--<modulesUtil:delayedModule mapping="ajaxModuleLawBottomStats" params="[hashtag:law.hashtag.decodeHashtag()]" elementId="idAjaxModuleLawBottomStats"/>--}%
    %{--<g:include controller="modules" action="bottomLawStats" params="[law:law]"/>--}%
    <a href="#main" class="smooth top">
        <span class="fa fa-caret-up fa-lg"></span>
        <g:message code="law.up"/>
    </a>
</content>