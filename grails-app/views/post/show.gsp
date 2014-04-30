<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${post.title}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="postMetaTags" model="[post:post]"/>
</head>


<content tag="mainContent">
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
    <div class="wrapper">
        <g:render template="/cluck/cluckUsers" model="[post:post]"/>
    </div>
    <g:render template="/cluck/footerCluck" model="[post:post]"/>
</article><!-- /article -->

<g:render template="relatedPosts" model="[relatedPosts:relatedPost]"/>
<g:render template="postComments" model="[post:post]"/>

</content>

<content tag="cColumn">
<section class="boxes noted likes">
<h1><g:message code="post.show.boxes.like.title"/></h1>
<p class="text-left"><g:message code="post.show.boxes.like.description"/> </p>
<div class="likesContainer">
    <div id="m-callback-done">
        <span class="likesCounter">124</span> impulsos
        <span class="arrowLike"></span>
    </div>
    <div class="progress">
        <div class="progress-bar" role="progressbar" aria-valuetransitiongoal="124" aria-valuemin="100" aria-valuemax="150"></div>
    </div>
</div>
<div class="sponsor">
    <ul class="user-list-images">
        <li itemtype="http://schema.org/Person" itemscope="" itemprop="contributor">
            <span data-toggle="popover" role="button" rel="popover" class="popover-trigger">
                <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg">
            </span>
            <!-- POPOVER PARA IMÁGENES USUARIOS -->
            <div class="popover">
                <button aria-hidden="true" class="close" type="button"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <a rel="nofollow" class="hidden" href="#">Mostrar usuario</a>
                <div class="popover-user">
                    <div itemtype="http://schema.org/Person" itemscope="" class="user">
                        <a itemprop="url" href="#">
                            <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">Nombre usuario</span>
                        </a>
                        <span class="user-type">
                            <small>Activista digital</small>
                        </span>
                    </div><!-- /user -->
                    <button id="follow" class="btn btn-blue btn-xs allow" type="button">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                    <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                    <ul class="infoActivity clearfix">
                        <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                        <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                        <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                        <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                    </ul>

                </div><!-- /popover-user -->

            </div>
            <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
        </li>
        <li itemtype="http://schema.org/Person" itemscope="" itemprop="contributor">
            <span data-toggle="popover" role="button" rel="popover" class="popover-trigger">
                <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg">
            </span>
            <!-- POPOVER PARA IMÁGENES USUARIOS -->
            <div class="popover">
                <button aria-hidden="true" class="close" type="button"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <a rel="nofollow" class="hidden" href="#">Mostrar usuario</a>
                <div class="popover-user">
                    <div itemtype="http://schema.org/Person" itemscope="" class="user">
                        <a itemprop="url" href="#">
                            <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">Nombre usuario</span>
                        </a>
                        <span class="user-type">
                            <small>Activista digital</small>
                        </span>
                    </div><!-- /user -->
                    <button id="follow" class="btn btn-blue btn-xs allow" type="button">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                    <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                    <ul class="infoActivity clearfix">
                        <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                        <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                        <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                        <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                    </ul>

                </div><!-- /popover-user -->

            </div>
            <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
        </li>
        <li itemtype="http://schema.org/Person" itemscope="" itemprop="contributor">
            <span data-toggle="popover" role="button" rel="popover" class="popover-trigger">
                <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg">
            </span>
            <!-- POPOVER PARA IMÁGENES USUARIOS -->
            <div class="popover">
                <button aria-hidden="true" class="close" type="button"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <a rel="nofollow" class="hidden" href="#">Mostrar usuario</a>
                <div class="popover-user">
                    <div itemtype="http://schema.org/Person" itemscope="" class="user">
                        <a itemprop="url" href="#">
                            <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">Nombre usuario</span>
                        </a>
                        <span class="user-type">
                            <small>Activista digital</small>
                        </span>
                    </div><!-- /user -->
                    <button id="follow" class="btn btn-blue btn-xs allow" type="button">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                    <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                    <ul class="infoActivity clearfix">
                        <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                        <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                        <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                        <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                    </ul>

                </div><!-- /popover-user -->

            </div>
            <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
        </li>
        <li itemtype="http://schema.org/Person" itemscope="" itemprop="contributor">
            <span data-toggle="popover" role="button" rel="popover" class="popover-trigger">
                <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg">
            </span>
            <!-- POPOVER PARA IMÁGENES USUARIOS -->
            <div class="popover">
                <button aria-hidden="true" class="close" type="button"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <a rel="nofollow" class="hidden" href="#">Mostrar usuario</a>
                <div class="popover-user">
                    <div itemtype="http://schema.org/Person" itemscope="" class="user">
                        <a itemprop="url" href="#">
                            <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">Nombre usuario</span>
                        </a>
                        <span class="user-type">
                            <small>Activista digital</small>
                        </span>
                    </div><!-- /user -->
                    <button id="follow" class="btn btn-blue btn-xs allow" type="button">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
                    <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
                    <ul class="infoActivity clearfix">
                        <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                        <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                        <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                        <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                        <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
                    </ul>

                </div><!-- /popover-user -->

            </div>
            <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
        </li>
        <li>
            <span data-toggle="popover" role="button" rel="popover" class="popover-trigger more-users">
                <span class="sr-only">Ver más patrocinadores</span>
                <span class="counter">+121</span>
            </span>
            <!-- POPOVER PARA SACAR LISTAS DE USUARIOS -->
            <div class="popover">
                <button aria-hidden="true" class="close" type="button"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <a rel="nofollow" class="hidden" href="#">Mostrar lista de usuarios</a>
                <div class="popover-user-list">
                    <p>Están participando...</p>
                    <div class="scroll">
                        <ul>
                            <li itemtype="http://schema.org/Person" itemscope="" class="user">
                                <a itemprop="url" href="#">
                                    <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">Nombre usuario</span>
                                </a>
                                <span class="user-type">
                                    <small>Activista digital</small>
                                </span>
                            </li><!-- /.user -->
                            <li itemtype="http://schema.org/Person" itemscope="" class="user">
                                <a itemprop="url" href="#">
                                    <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">Nombre usuario</span>
                                </a>
                                <span class="user-type">
                                    <small>Activista digital</small>
                                </span>
                            </li><!-- /.user -->
                            <li itemtype="http://schema.org/Person" itemscope="" class="user">
                                <a itemprop="url" href="#">
                                    <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">Nombre usuario</span>
                                </a>
                                <span class="user-type">
                                    <small>Activista digital</small>
                                </span>
                            </li><!-- /.user -->
                            <li itemtype="http://schema.org/Person" itemscope="" class="user">
                                <a itemprop="url" href="#">
                                    <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">Nombre usuario</span>
                                </a>
                                <span class="user-type">
                                    <small>Activista digital</small>
                                </span>
                            </li><!-- /.user -->
                            <li itemtype="http://schema.org/Person" itemscope="" class="user">
                                <a itemprop="url" href="#">
                                    <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">Nombre usuario</span>
                                </a>
                                <span class="user-type">
                                    <small>Activista digital</small>
                                </span>
                            </li><!-- /.user -->
                        </ul>
                    </div><!-- /.contenedor scroll -->
                </div><!-- /popover-user-list -->

            </div>
            <!-- FIN POPOVER PARA SACAR LISTAS DE USUARIOS -->
        </li>
    </ul><!-- /.user-list-images -->
</div>
<form id="drive">
    <a class="btn btn-blue btn-lg btn-block" href="#">Impulsa esta propuesta <br><small>es tu momento de hablar</small></a>
    <div class="form-group">
        <label class="checkbox-inline"><input type="checkbox" value="public" id="publico"> Quiero que mi impulso sea público</label>
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