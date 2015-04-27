<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="edit-post" />
</head>

<content tag="intro">
    <h1><g:message code="post.edit.step3.intro.head"/></h1>
    <p><g:message code="post.edit.step3.intro.subHead"/></p>
</content>

<content tag="mainContent">
    <article class="kakareo post" itemscope itemtype="http://schema.org/Article" role="article" data-cluck-postId="${post.id}">
        <g:render template="/cluck/cluckMenuEditPost" model="[post:post]"/>

        <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
        <h1>${post.title} <g:link mapping="projectShow" params="${post.project.encodeAsLinkProperties()}">${post.project.hashtag}</g:link> </h1>
        <g:render template="/post/postUsers" model="[post:post]"/>
        <postUtil:postShowMultimedia post="${post}"/>
        <g:render template="/cluck/footerCluck" model="[post:post, displayingColumnC:false]"/>
        <p>
            ${raw(post.text.encodeAsRemovingScriptTags().replaceAll('<br>','</p><p>'))}
        </p>
    </article>

    <h2><g:message code="post.edit.step3.info.title"/></h2>
    %{--<p class="lead"><g:message code="post.edit.step3.info.content.big"/></p>--}%
    <p><g:message code="post.edit.step3.info.content.big"/></p>
    <p><g:message code="post.edit.step3.info.content.small"/></p>

    <ul class="btns">
        <g:if env="production">
            %{--DESACTIVADO PROCESO PAGO EN PRODUCCION--}%
        </g:if>
        <g:else>
            <li>
                <g:link mapping="postPayPost" class="btn btn-blue btn-lg" params="${post.encodeAsLinkProperties()}">
                    <g:message code="post.edit.step3.promoteButton"/>
                </g:link>
            </li>
        </g:else>
        <li>
            <g:link mapping="postShow" class="btn btn-grey-light btn-lg" params="${post.encodeAsLinkProperties()}">
                Ver mi propuesta
            </g:link>
        </li>
    </ul>
    <g:if test="${gamificationData}">
        <script>
            $(function(){
                var gamification = {
                    title: "${gamificationData.title}",
                    text:"${gamificationData.text}",
                    eggs:${gamificationData.eggs},
                    plumes:${gamificationData.plumes},
                    corns:${gamificationData.corns}
                }
                karma.open(gamification)
            });
        </script>
    </g:if>
</content>

<content tag="cColumn">
    <section class="boxes vote drive">
        <g:render template="likesContainer" model="[post:post]"/>
        <g:render template="/post/postSocialShare" model="[post:post]"/>
    </section>
</content>
