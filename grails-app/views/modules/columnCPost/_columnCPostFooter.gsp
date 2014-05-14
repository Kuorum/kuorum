<footer class="row">
    <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
        <li itemprop="keywords">
            <span class="fa ${postUtil.cssIconPostType(post:post)} fa-lg" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'cluck.footer.'+post.postType)}" rel="tooltip"></span><!-- icono -->
            <span class="sr-only"><g:message code="cluck.footer.${post.postType}"/></span><!-- texto que explica el icono y no es visible -->
        </li>
        <li class="sr-only" itemprop="datePublished">
            <kuorumDate:humanDate date="${post.dateCreated}"/>
        </li>
    </ul>
    <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
        <li class="read-later">
            <g:link mapping="postToggleFavorite" class="${postUtil.cssClassIfFavorite(post:post)}" params="${post.encodeAsLinkProperties()}" rel="nofollow">
                <span class="fa fa-bookmark fa-lg"></span>
                <span class="sr-only"><g:message code="cluck.footer.readLater"/></span>
            </g:link>
        </li>
        <li class="like-number">
            <span class="counter">${post.numVotes}</span>
            <meta itemprop="interactionCount" content="UserLikes:${post.numVotes}"><!-- pasarle el valor que corresponda -->
            <g:link mapping="postVoteIt" class="${postUtil.cssClassIfVoted(post:post)}" params="${post.encodeAsLinkProperties()}" rel="nofollow">
                <span class="fa fa-rocket fa-lg"></span>
                <span class="sr-only"><g:message code="cluck.footer.vote"/></span>
            </g:link>
        </li>

        <li class="kakareo-number">
            <span class="counter">${post.numClucks}</span>
            <g:link mapping="postCluckIt" class="${postUtil.cssClassIfClucked(post:post)}" params="${post.encodeAsLinkProperties()}" rel="nofollow" >
                <span class="icon-megaphone fa-lg"></span><span class="sr-only"><g:message code="cluck.footer.cluckIt"/></span>
            </g:link>
        </li>

        <li class="more-actions">
            <span class="popover-trigger more-actions" rel="popover" role="button" data-toggle="popover">
                <span class="fa fa-plus"></span> <span class="sr-only">Más acciones</span>
            </span>
            <!-- POPOVER MÁS ACCIONES -->
            <div class="popover">
                <a href="#" class="hidden" rel="nofollow">Mostrar lista de usuarios</a>
                <div class="popover-more-actions">
                    <ul>
                        <li><a href="#">Patrocinar</a></li>
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
</footer>