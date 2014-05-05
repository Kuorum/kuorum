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
    <div class="author ${important}">
        <postUtil:politiciansHeadPost post="${post}"/>
        <article class="kakareo post" role="article" itemscope itemtype="http://schema.org/Article" data-cluck-postId="${post.id}">
            <div class="wrapper">
                <g:render template="/cluck/cluckMain" model="[post:post]"/>
            </div>
            <g:render template="/cluck/footerCluck" model="[cluck:post.firstCluck]"/>

            <div class="row options">
                <div class="col-xs-12 col-sm-6 col-md-6 editPost">
                    <postUtil:ifPostIsEditable post="${post}">
                        <g:link mapping="postEdit" params="${post.encodeAsLinkProperties()}">
                            <span class="fa fa-edit fa-lg"></span><g:message code="post.show.editLink.${post.postType}"/>
                        </g:link>
                    </postUtil:ifPostIsEditable>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-6 leerLey">
                    <g:if test="${post.pdfPage}">
                        <a target="_blank" href="${post.law.urlPdf}#page=${post.pdfPage}"><g:message code="post.show.pdfLink"/></a>
                    </g:if>
                </div>
            </div>
            <p>${raw(post.text.replaceAll('<br>','</p><p>'))}</p>
            <g:if test="${!post.debates.isEmpty()}">
                <g:render template="debates/postDebates" model="[post:post]"/>
            </g:if>
            <div class="wrapper">
                <g:render template="/cluck/cluckUsers" model="[post:post]"/>
            </div>
            <g:render template="/cluck/footerCluck" model="[post:post]"/>
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
        <form id="drive">
            <g:link mapping="postVoteIt" class="btn  ${important?:'btn-blue'} btn-lg btn-block" params="${post.encodeAsLinkProperties()}" data-postId="${post.id}">
                Impulsa esta propuesta <br><small>es tu momento de hablar</small>
            </g:link>
            <div class="form-group">
                <label class="checkbox-inline"><input type="checkbox" name="anonymous" value="private"/> Quiero que mi impulso sea privado</label>
            </div>
        </form>

        <p>Comparte en las redes sociales</p>
        <ul class="social">
            <li><a href="#"><span class="sr-only">Twitter</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-twitter fa-stack-1x"></span></span></a></li>
            <li><a href="#"><span class="sr-only">Facebook</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-facebook fa-stack-1x"></span></span></a></li>
            <li><a href="#"><span class="sr-only">LinkedIn</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-linkedin fa-stack-1x"></span></span></a></li>
            <li><a href="#"><span class="sr-only">Google+</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-google-plus fa-stack-1x"></span></span></a></li>
        </ul>

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