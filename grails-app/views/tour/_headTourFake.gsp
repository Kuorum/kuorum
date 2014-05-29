<!-- HEADER CONECTADO -->
<header id="header" class="row" role="banner">
    <nav class="navbar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <g:render template="/layouts/brandAndLogo"/>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <g:render template="/layouts/searchHeadForm"/>
                <g:render template="/layouts/discoverHead"/>

                <ul class="nav navbar-nav navbar-right">
                    <li class="underline" itemscope itemtype="http://schema.org/Person">
                        <a href="#" class="navbar-link user-area">
                            <span itemprop="name">Nombre usuario</span> <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                        </a>
                    </li>
                    <li class="dropdown underline">
                        <a data-target="#" href="/dashboard.htm" id="open-user-options" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
                            <span class="fa fa-gear fa-lg"></span>
                            <span class="visible-xs">Opciones de usuario</span>
                        </a>
                        <ul id="user-options" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-options" role="menu">
                            <li>Mi pefil</li>
                            <li><a href="#">Editar mis datos</a></li>
                            <li><a href="#">Cambiar mi contraseña</a></li>
                            <li><a href="#">Alertas por email</a></li>
                            <li><a href="#"><span id="post-sin-leer">Post marcados para leer</span> <span class="badge" role="log" aria-labelledby="post-sin-leer" aria-live="assertive" aria-relevant="additions">2</span></a></li>
                            <li><a href="#"><span id="mis-post">Mis post <span class="badge" role="log" aria-labelledby="mis-post" aria-live="assertive" aria-relevant="additions">12</span></a></li>
                            <li><a href="#">Kuorum store</a></li>
                            <li><a href="#">Apoya esta propuesta</a></li>
                            <li><a href="#">Es tu momento de hablar</a></li>
                            <li><a href="#">Centro de notificaciones</a></li>
                            <li><a href="#"><span id="messages-user">Mensajes</span> <span class="badge" role="log" aria-labelledby="messages-user" aria-live="assertive" aria-relevant="additions">7</span></a></li>
                            <li><a href="#">Cerrar sesión y salir</a></li>
                        </ul>
                    </li>
                    <li class="dropdown underline box">
                        <a href="#" class="dropdown-toggle dropdown-menu-right navbar-link" id="open-user-messages" data-toggle="dropdown" role="button">
                            <span class="fa fa-envelope fa-lg"></span>
                            <span class="visible-xs" id="messages">Mensajes</span>
                            <span class="badge pull-right" role="log" aria-labelledby="messages" aria-live="assertive" aria-relevant="additions">11</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-messages" role="menu">
                            <li class="hidden-xs">Mensajes</li>
                            <li class="new">
                                <span itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                </span>
                                <span class="fecha"><small><time class="timeago" class="timeago" datetime="2014-04-04T09:00:10+02:00">hace 2 horas</time></small></span>
                                <span class="text-message">Tempor eros ut libero hendre vestibil. Nullatin suada dignissim. <a href="#" class="read">Ver mensaje</a></span>
                            </li>
                            <li>
                                <span itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                </span>
                                <span class="fecha"><small><time class="timeago" datetime="2014-04-03T020:20:10+02:00">hace 9 horas</time></small></span>
                                <span class="text-message">Class aptent taciti socios. <a href="">Ver mensaje</a></span>
                            </li>
                            <li>
                                <span itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                </span>
                                <span class="fecha"><small><time class="timeago" datetime="2014-04-03T020:20:10+02:00">hace 1 día</time></small></span>
                                <span class="text-message">Aptent taciti socios. Congue vitae metus et malesuada.<a href="">Ver mensaje</a></span>
                            </li>
                            <li>
                                <ul class="inline clearfix">
                                    <li><a href="#" class="pull-left">Ver todos</a></li>
                                    <li><a href="#" class="pull-right">Enviar un mensaje</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <li class="dropdown underline box">
                        <a href="#" class="dropdown-toggle dropdown-menu-right navbar-link" id="open-user-notifications" data-toggle="dropdown" role="button">
                            <span class="fa fa-bell fa-lg"></span>
                            <span class="visible-xs" id="alerts">Notificaciones</span>
                            <span class="badge pull-right" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">8</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-notifications" role="menu">
                            <li class="hidden-xs">Notificaciones</li>
                            <li class="new">
                                <span class="text-notification">Aenean tempor eros ut libero hendrerit aptent malesuada dignissim nulla tincidunt males hentit.</span>
                                <span class="actions clearfix">
                                    <span class="pull-right">
                                        <a href="#" class="btn btn-xs">Responder</a>
                                    </span>
                                </span>
                            </li>
                            <li>
                                <span itemscope itemtype="http://schema.org/Person">
                                    <a href="#" itemprop="url">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                    </a>
                                </span>
                                <span class="text-notification">Aptent taciti vestibulum nulla tincidunt malesuada dignissim.</span>
                            </li>
                            <li>
                                <ul class="inline clearfix">
                                    <li class="text-center"><a href="#">Ver todas las notificaciones</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

</header>
<!-- FIN HEADER CONECTADO -->