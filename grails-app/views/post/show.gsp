<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${post.title}</title>
    <meta name="layout" content="columnCFloatingLayout">
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
    <section class="boxes vote drive">
        <g:render template="likesContainer" model="[post:post]"/>
        <div class="sponsor">
            <userUtil:showListUsers users="${usersVotes}" visibleUsers="5" messagesPrefix="post.show.boxes.like.userList"/>
        </div>
        <g:render template="/post/postVotePostButton" model="[post:post, userVote:userVote]"/>

        <p>Comparte en las redes sociales</p>
        <ul class="social">
            <li><a href="#"><span class="sr-only">Twitter</span><span class="fa fa-twitter fa-2x"></span></a></li>
            <li><a href="#"><span class="sr-only">Facebook</span><span class="fa fa-facebook fa-2x"></span></a></li>
            <li><a href="#"><span class="sr-only">LinkedIn</span><span class="fa fa-linkedin fa-2x"></span></a></li>
            <li><a href="#"><span class="sr-only">Google+</span><span class="fa fa-google-plus fa-2x"></span></a></li>
        </ul>
    </section>
    %{--<section class="boxes noted likes ${important}">--}%
        %{--<h1><g:message code="post.show.boxes.like.title"/></h1>--}%
        %{--<p class="text-left"><g:message code="post.show.boxes.like.description"/> </p>--}%
        %{--<div class="sponsor">--}%
            %{--<userUtil:showListUsers users="${usersVotes}" visibleUsers="5" messagesPrefix="post.show.boxes.like.userList"/>--}%
        %{--</div>--}%
        %{--<g:render template="/post/postVotePostButton" model="[post:post, userVote:userVote, important:important]"/>--}%

        %{--<g:render template="/post/postSocialShare" model="[post:post]"/>--}%

    %{--</section>--}%


</content>

%{--<content tag="footerStats">--}%

    %{--<modulesUtil:delayedModule mapping="ajaxModuleProjectBottomStats" params="[hashtag:project.hashtag.decodeHashtag()]" elementId="idAjaxModuleProjectBottomStats"/>--}%
    %{--<g:include controller="modules" action="bottomProjectStats" params="[project:project]"/>--}%
    %{--<a href="#main" class="smooth top">--}%
        %{--<span class="fa fa-caret-up fa-lg"></span>--}%
        %{--<g:message code="project.up"/>--}%
    %{--</a>--}%
%{--</content>--}%