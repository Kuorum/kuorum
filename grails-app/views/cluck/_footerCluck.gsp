<%@ page import="kuorum.core.model.PostType" %>
<footer class="row">
    <g:render template="/post/footerPostType" model="[post:post]"/>
    <sec:ifLoggedIn>
        <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
            <li class="read-later">
                <g:link mapping="postToggleFavorite" class="${postUtil.cssClassIfFavorite(post:post)}" params="${post.encodeAsLinkProperties()}" rel="nofollow">
                    <span class="fa fa-bookmark fa-lg"></span><span class="hidden-xs"><g:message code="cluck.footer.readLater"/></span>
                </g:link>
            </li>

            <li class="like-number">
                <span class="counter">${post.numVotes}</span>
                <meta itemprop="interactionCount" content="UserLikes:${post.numVotes}"><!-- pasarle el valor que corresponda -->
                <g:link mapping="postVoteIt" class="${postUtil.cssClassIfVoted(post:post)} action drive" params="${post.encodeAsLinkProperties()}" rel="nofollow">
                    <span class="fa fa-rocket fa-lg"></span><span class="hidden-xs"><g:message code="cluck.footer.vote"/></span>
                </g:link>
            </li>

            <li class="kakareo-number">
                <span class="popover-trigger more-users counter" rel="popover" role="button" data-toggle="popover">${post.numClucks}</span>
                <!-- POPOVER PARA SACAR LISTAS DE USUARIOS -->
                <div class="popover">
                    <a href="#" class="hidden" rel="nofollow">Lista de usuarios que han impulsado</a>
                    <div class="popover-user-list">
                        <p>Han kakareado...</p>
                        <div class="scroll">
                            <ul>
                                <li class="user" itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="link"><a href="#">Ver todos</a></li>
                            </ul>
                        </div><!-- /.contenedor scroll -->
                    </div><!-- /popover-user-list -->
                </div>
                <!-- FIN POPOVER PARA SACAR LISTAS DE USUARIOS -->
                <g:link mapping="postCluckIt" class="action cluck ${postUtil.cssClassIfClucked(post:post)}" params="${post.encodeAsLinkProperties()}" rel="nofollow">
                    <span class="fa fa-bullhorn fa-lg"></span><span class="hidden-xs"><g:message code="cluck.footer.cluckIt"/></span>
                </g:link>
            </li>

            <li class="more-actions">
                <span class="popover-trigger more-actions" rel="popover" role="button" data-toggle="popover">
                    <span class="fa fa-plus"></span> <span class="sr-only"><g:message code="cluck.footer.moreActions"/></span>
                </span>
                <!-- POPOVER MÁS ACCIONES -->
                <div class="popover">
                    <div class="popover-more-actions">
                        <ul>
                            <li><g:link mapping="postPayPost" params="${post.encodeAsLinkProperties()}"><g:message code="cluck.footer.promote"/></g:link></li>
                            <li><a href="#">Compartir en Facebook</a></li>
                            <li><a href="#">Compartir en Twitter</a></li>
                            <li><a href="#">Enviar por email</a></li>
                            <li class="kakareo-number"><a href="#" class="action cluck">Kakarear</a></li>
                            <li class="like-number"><a href="#" class="action drive">Impulsar</a></li>
                            <li class="read-later"><a href="#" class="enabled allow">Leer más tarde</a></li>
                            <li class="mark"><a href="#" class="enabled allow">Marcar como inapropiado</a></li>
                        </ul>
                    </div><!-- /popover-more-actions -->

                </div>
                <!-- FIN POPOVER MÁS ACCIONES -->
            </li>
        </ul>
    </sec:ifLoggedIn>
</footer>
