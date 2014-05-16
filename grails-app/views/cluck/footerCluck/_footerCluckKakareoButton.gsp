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
        <span class="icon-megaphone fa-lg"></span>
        <span class="${displayingColumnC?'sr-only':'hidden-xs'}"><g:message code="cluck.footer.cluckIt"/></span>
    </g:link>
</li>