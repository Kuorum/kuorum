<%@ page import="kuorum.core.FileGroup" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="editPostLayout">
    <parameter name="extraCssContainer" value="edit-post" />
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
        <footer class="row">
            <g:render template="/cluck/footerCluck/footerCluckPostType" model="[post:post, displayingColumnC:false]"/>
            <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
                <!-- <li class="read-later">
                                        <a href="#"><span class="fa fa-bookmark fa-lg"></span><span class="hidden-xs">Leer después</span></a>
                                    </li>
                                    <li class="like-number">
                                        <span class="counter">1</span><meta itemprop="interactionCount" content="UserLikes:1"><a href="#" class="action drive disabled"><span class="fa fa-rocket fa-lg"></span><span class="hidden-xs">Impulsar</span></a>
                                    </li>
                                    <li class="kakareo-number">
                                        <span class="counter">1</span><a href="#" class="action cluck disabled"><span class="icon-megaphone fa-lg"></span><span class="hidden-xs">Kakarear</span></a>
                                    </li>
                                    <li class="more-actions">
                                        <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover"><span class="fa fa-plus"></span> <span class="sr-only">Más acciones</span></a>
                                    </li> -->
            </ul>
        </footer>

        <p>Para que llegue mas y más lejos, at vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.</p>
        <p>Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.</p>
        <h2>Temporibus autem quibusdam et aut officiis debitis</h2>
        <p>Quien quiera, sólo tiene que adquirir el compromiso, at vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio.</p>
        <p>Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.</p>
        <p>Quien quiera, sólo tiene que adquirir el compromiso, at vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio.</p>
        <p>Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.</p>
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
        <g:link mapping="lawShow" params="${post.law.encodeAsLinkProperties()}">${post.law.hashtag}</g:link>
        <h1>${post.law.shortName}</h1>
        <p>${post.law.realName}</p>
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
