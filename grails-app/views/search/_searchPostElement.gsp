<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post">
    <div class="link-wrapper">
        <g:link mapping="postShow" class="hidden" params="${solrPost.encodeAsLinkProperties()}">Ir al post</g:link>
        <h1>${raw(solrPost.highlighting.name)} ${raw(solrPost.highlighting.hashtagLaw)}</h1>
        <p>${raw(solrPost.highlighting.text)}</p>
        <div itemtype="http://schema.org/Person" itemscope itemprop="author" class="user author">
            <span data-toggle="popover" role="button" rel="popover" class="popover-trigger">
                <img itemprop="image" class="user-img" alt="nombre" src="images/user.jpg"><span itemprop="name">${raw(solrPost.highlighting.owner)}</span>
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
            <span class="user-type">
                <small>Activista digital</small>
            </span>
        </div>

    </div>
</article>