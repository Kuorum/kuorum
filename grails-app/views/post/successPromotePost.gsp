<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="edit-post" />
</head>

<content tag="intro">
</content>

<content tag="mainContent">
    <article class="kakareo post promo tks" role="article" itemscope itemtype="http://schema.org/Article">
        <h1><g:message code="post.promote.step3.title"/></h1>
        <h2><g:message code="post.promote.step3.description"/></h2>
        <ol class="next">
            <g:each in="${1..3}" var="i">

            </g:each>
            <li>
                <span class="badge">1</span>
                <h3><g:message code="post.promote.step3.budge.1.title" args="[numMails]"/></h3>
                <p><g:message code="post.promote.step3.budge.1.p1"/></p>
                <p><g:message code="post.promote.step3.budge.1.p2"/></p>
            </li>
            <li>
                <span class="badge">2</span>
                <h3><g:message code="post.promote.step3.budge.2.title"/></h3>
                <p><g:message code="post.promote.step3.budge.2.p1"/></p>
                <p><g:message code="post.promote.step3.budge.2.p2"/></p>
            </li>
            <li>
                <span class="badge">3</span>
                <h3><g:message code="post.promote.step3.budge.3.title"/></h3>
                <p><g:message code="post.promote.step3.budge.3.p1"/></p>
                <p><g:message code="post.promote.step3.budge.3.p2"/></p>
            </li>
        </ol>
        <h4><g:message code="post.promote.step3.inviteFriends"/></h4>
        <ul class="invite clearfix">
            <li><a class="btn mail" href="#"><span class="fa fa-envelope fa-lg"></span> Enviar emails</a></li>
            <li><a class="btn tw" href="#"><span class="fa fa-twitter fa-lg"></span> Publicar</a></li>
            <li><a class="btn fb" href="#"><span class="fa fa-facebook fa-lg"></span> Compartir</a></li>
        </ul>

    </article><!-- /article -->
</content>

<content tag="cColumn">
    <g:render template="/modules/recommendedPosts" model="[recommendedPost:[post], title:message(code:'post.promote.columnC.postPromoted.title'),specialCssClass:'']"/>
</content>