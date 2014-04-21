<section class="boxes edit-user">
    <h1 class="sr-only"><g:message code="dashboard.userProfile.user"/> </h1>
    <a href="" class="text-right edit">Editar</a>
    <div class="user" itemscope itemtype="http://schema.org/Person">
        <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url">
            <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img big" itemprop="image">
            <div class="personal">
                <span itemprop="name" class="name">${user.name}</span>
                <span class="user-type"><small><userUtil:roleName user="${user}"/></small></span>
            </div>
        </g:link>
        <ul class="activity">
            <li class="followers">
                <span class="popover-trigger more-users" rel="popover" role="button" data-toggle="popover"><span>${numFollowers}</span></span>
                <g:message code="dashboard.userProfile.followers"/>
            <!-- POPOVER PARA SACAR LISTAS DE USUARIOS -->
                <div class="popover">
                    <a href="#" class="hidden" rel="nofollow">Mostrar lista de seguidores</a>
                    <div class="popover-user-list">
                        <p>Seguidores...</p>
                        <div class="scroll">
                            <ul>
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
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



            <li class="following">
                <span class="popover-trigger more-users" rel="popover" role="button" data-toggle="popover" data-original-title="" title=""><span>${numFollowing}</span></span>
                <g:message code="dashboard.userProfile.following"/>
            <!-- POPOVER PARA SACAR LISTAS DE USUARIOS -->
                <div class="popover">
                    <a href="#" class="hidden" rel="nofollow">Mostrar lista de seguidores</a>
                    <div class="popover-user-list">
                        <p>Siguiendo...</p>
                        <div class="scroll">
                            <ul>
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                    <span class="user-type">
                                        <small>Activista digital</small>
                                    </span>
                                </li><!-- /.user -->
                                <li class="user" itemscope="" itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
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
            <li class="posts"><span>${numPosts}</span> <g:message code="dashboard.userProfile.posts"/></li>
        </ul>
    </div>
</section>