<%@ page import="kuorum.core.FileGroup" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="edit-post" />
</head>

<content tag="intro">
    <h1><g:message code="post.edit.step2.intro.head"/></h1>
    <p><g:message code="post.edit.step2.intro.subHead"/></p>
</content>

<content tag="mainContent">
    <g:set var="multimedia" value=""/>
    <postUtil:ifHasMultimedia post="${post}">
        <g:set var="multimedia" value="multimedia"/>
    </postUtil:ifHasMultimedia>
    <article class="kakareo post ${multimedia}" role="article" itemscope itemtype="http://schema.org/Article">
        <g:render template="/cluck/cluckMain" model="[post:post]"/>
        <g:render template="/cluck/footerCluckNoAction" model="[post:post]"/>
        <g:render template="postBody" model="[post:post]"/>
        <g:render template="/cluck/footerCluckNoAction" model="[post:post]"/>
    </article><!-- /article -->

    <ul class="btns">
        <li>
            <g:link mapping="postPublish" class="btn btn-blue btn-lg" params="${post.encodeAsLinkProperties()}">
                <g:message code="post.edit.step2.publish" args="[g.message(code: 'kuorum.core.model.PostType.'+post.postType)]"/> <br>
                <small><g:message code="post.edit.step2.publish.youCan"/> </small>
            </g:link>
        </li>
        <li>
            <g:link mapping="postEdit" class="cancel" params="${post.encodeAsLinkProperties()}">
                <g:message code="post.edit.step2.backAndEdit"/>
            </g:link>
        </li>
        <li>
            <g:link mapping="home" class="cancel">
                <g:message code="post.edit.step2.saveAndExit"/>
            </g:link>
        </li>
    </ul>
</content>

<content tag="cColumn">
    <section class="boxes noted">
        <g:link mapping="projectShow" target="_blank" params="${post.project.encodeAsLinkProperties()}">${post.project.hashtag}</g:link>
        <h1>${post.project.shortName}</h1>
        <p>${post.project.realName}</p>
    </section>
    <section class="boxes btns">
        <g:link mapping="postPublish" class="btn btn-blue btn-lg btn-block" params="${post.encodeAsLinkProperties()}">
            <g:message code="post.edit.step2.publish" args="[g.message(code: 'kuorum.core.model.PostType.'+post.postType)]"/> <br>
            <small><g:message code="post.edit.step2.publish.youCan"/> </small>
        </g:link>
        <g:link mapping="postEdit" class="cancel" params="${post.encodeAsLinkProperties()}">
            <g:message code="post.edit.step2.backAndEdit"/>
        </g:link>
        <g:link mapping="home" class="cancel">
            <g:message code="post.edit.step2.saveAndExit"/>
        </g:link>
    </section>

</content>
