<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="editPostLayout">
</head>

<content tag="intro">
    <h1><g:message code="post.edit.step3.intro.head"/></h1>
    <p><g:message code="post.edit.step3.intro.subHead"/></p>
</content>

<content tag="mainContent">
    <article class="kakareo post sponsor" role="article" itemscope itemtype="http://schema.org/Article" data-cluck-postId="${post.id}">
        <div class="wrapper">
            <h1>${post.title} <g:link mapping="lawShow" params="${post.law.encodeAsLinkProperties()}">${post.law.hashtag}</g:link></h1>
            <div class="main-kakareo row">
                <div class="col-md-5 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                    <userUtil:showUser user="${post.owner}" showRole="true"/>
                </div><!-- /autor -->

                <div class="col-md-7 text-right sponsor">
                    <!-- está vacío pero se deja la estructura igual que en un kakareo -->
                </div><!-- /.sponsor -->
            </div><!-- /.main-kakareo -->
        </div>
        <g:render template="/cluck/footerCluck" model="[cluck:post,displayingColumnC:false]"/>
    </article><!-- /article -->

    <h2>Para qué patrocinar</h2>
    <p class="lead">Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.</p>
    <p>Para que llegue mas y más lejos, at vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.</p>
    <p>Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.</p>

    <h2>Invita a tus amigos para que hagan crecer la propuesta</h2>
    <ul class="socialSponsor clearfix">
        <li><a class="btn tw" href="#"><span class="fa fa-twitter fa-lg"></span> Publicar</a></li>
        <li><a class="btn fb" href="#"><span class="fa fa-facebook fa-lg"></span> Compartir</a></li>
        <li><a class="btn gog" href="#"><span class="fa fa-google-plus fa-lg"></span> Buscar contactos</a></li>
    </ul>

    <ul class="btns">
        <li>
            <g:link mapping="postPayPost" class="btn btn-blue btn-lg" params="${post.encodeAsLinkProperties()}">
                Patrocina mi propuesta
            </g:link>
        </li>
        <li>
            <g:link mapping="postShow" class="btn btn-grey-light btn-lg" params="${post.encodeAsLinkProperties()}">
                Ver mi propuesta
            </g:link>
        </li>
    </ul>
</content>

<content tag="cColumn">
    <section class="boxes noted likes">
        <h1><g:message code="post.edit.step3.firstVoteTitle"/> </h1>
        <g:render template="likesContainer" model="[post:post]"/>
        <p>Comparte en las redes sociales</p>
        <ul class="social">
            <li><a href="#"><span class="sr-only">Twitter</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-twitter fa-stack-1x"></span></span></a></li>
            <li><a href="#"><span class="sr-only">Facebook</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-facebook fa-stack-1x"></span></span></a></li>
            <li><a href="#"><span class="sr-only">LinkedIn</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-linkedin fa-stack-1x"></span></span></a></li>
            <li><a href="#"><span class="sr-only">Google+</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-google-plus fa-stack-1x"></span></span></a></li>
        </ul>

    </section>
</content>
