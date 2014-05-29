<g:applyLayout name="main">

<head>
    <title>Tour - Paso 1</title>
</head>

<body itemscope itemtype="http://schema.org/WebPage">
<g:render template="headTourFake"/>

<div class="row main">

<div class="container-fluid">
<div class="row">
<section id="main" class="col-xs-12 col-sm-8 col-md-8" role="main">
    <ul id="list-kakareos-id" class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">
        <g:each in="${fakeClucks}" var="cluck">
            <g:render template="fakeCluck" model="[cluck:cluck]"/>
        </g:each>
    </ul>
</section>

<aside class="col-xs-12 col-sm-4 col-md-4" role="complementary">
    <g:render template="/modules/userProfile" model="[user:user, numPosts:10]"/>
    <g:render template="/modules/userProfileAlerts" model="[alerts:[]]"/>
    <modulesUtil:recommendedPosts title="${message(code:"modules.recommendedPosts.title")}"/>

<section class="boxes follow">
    <h1>A quién seguir</h1>
    <div class="kakareo follow">
        <ul class="user-list-followers">
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
            <li itemscope itemtype="http://schema.org/Person">
                <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                </a>
            </li>
        </ul><!-- /.user-list-followers -->
    </div>
</section>

<section class="boxes guay pending">
    <h1>
        <span class="badgeContainer">
            <span class="fa fa-bookmark"></span>
            <span class="badge" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">8</span>
        </span>
        <span class="bigger">Post pendientes de leer</span>
    </h1>
    <ul class="kakareo-list">
        <!-- KAKAREO NORMAL Y DESTACADO -->
        <li itemscope itemtype="http://schema.org/Article">
            <article class="kakareo" role="article">
                <div class="link-wrapper">
                    <a href="#" class="hidden">Ir al post</a>
                    <h1>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor <a href="#">#leyaborto</a></h1>
                    <div class="main-kakareo row">
                        <!-- cambia la clase col-md era 5 y pasa a ser 6 -->
                        <div class="col-md-6 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                        </div><!-- /autor -->

                    <!-- cambia la clase col-md era 7 y pasa a ser 6 -->
                        <div class="col-md-6 text-right sponsor">
                            Patrocinado por
                            <ul class="user-list-images">
                                <li itemprop="contributor" itemscope itemtype="http://schema.org/Person">
                                    <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                                    </a>
                                </li>
                            </ul><!-- /.user-list-images -->
                        </div><!-- /patrocinadores -->
                    </div>
                </div><!-- /.link-wrapper -->
                <footer class="row">
                    <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
                        <li itemprop="keywords">
                            <!-- en caso de ser Historia -->
                            <span class="fa fa-book fa-lg" data-toggle="tooltip" data-placement="bottom" title="Historia" rel="tooltip"></span>
                            <span class="sr-only">Esto es una Historia</span>
                        </li>
                        <li class="sr-only" itemprop="datePublished">
                            <time class="timeago" datetime="2014-04-07T13:40:50+02:00">hace 14 horas</time>
                        </li>
                    </ul>
                    <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
                        <li class="read-later">
                            <a href="#">
                                <span class="fa fa-bookmark fa-lg"></span>
                                <span class="sr-only">Leer después</span>
                            </a>
                        </li>
                        <li class="like-number">
                            <span class="counter">3222</span>
                            <meta itemprop="interactionCount" content="UserLikes:2"><!-- pasarle el valor que corresponda -->
                            <a href="#" class="action drive">
                                <span class="fa fa-rocket fa-lg"></span>
                                <span class="sr-only">Impulsar</span>
                            </a>
                        </li>

                        <li class="kakareo-number">
                            <span class="counter">5428</span>
                            <a href="#" class="action cluck">
                                <span class="fa fa-bullhorn fa-lg"></span>
                                <span class="sr-only">Kakarear</span>
                            </a>
                        </li>

                        <li class="more-actions">
                            <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                                <span class="fa fa-plus"></span> <span class="sr-only">Más acciones</span>
                            </span>
                        </li>
                    </ul>
                </footer>
            </article><!-- /article -->

        </li><!-- /kakareo normal y destacado -->

    <!-- KAKAREO AUTOR IMPORTANT -->
        <li class="author important" itemscope itemtype="http://schema.org/Article">
            <div class="user actor politic" itemprop="contributor" itemscope itemtype="http://schema.org/Person">
                <span itemprop="name">Nombre usuario</span>
                <a href="#" itemprop="url"><img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"></a>
            </div>
            <article class="kakareo" role="article">
                <div class="link-wrapper">
                    <a href="#" class="hidden">Ir al post</a>
                    <h1>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor <a href="#">#leyaborto</a></h1>
                    <div class="main-kakareo row">
                        <!-- cambia la clase col-md era 5 y pasa a ser 6 -->
                        <div class="col-md-6 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                            <a href="#" itemprop="url">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </a>
                        </div><!-- /autor -->

                    <!-- cambia la clase col-md era 7 y pasa a ser 6 -->
                        <div class="col-md-6 text-right sponsor">
                            Patrocinado por
                            <ul class="user-list-images">
                                <li itemprop="contributor" itemscope itemtype="http://schema.org/Person">
                                    <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                                        <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">
                                    </a>
                                </li>
                            </ul><!-- /.user-list-images -->
                        </div><!-- /patrocinadores -->
                    </div>
                </div><!-- /.link-wrapper -->
                <footer class="row">
                    <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
                        <li itemprop="keywords">
                            <!-- en caso de ser Historia -->
                            <span class="fa fa-book fa-lg" data-toggle="tooltip" data-placement="bottom" title="Historia" rel="tooltip"></span>
                            <span class="sr-only">Esto es una Historia</span>
                        </li>
                        <li class="sr-only" itemprop="datePublished">
                            <time class="timeago" datetime="2014-04-07T13:40:50+02:00">hace 16 días</time>
                        </li>
                    </ul>
                    <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
                        <li class="read-later">
                            <a href="#">
                                <span class="fa fa-bookmark fa-lg"></span>
                                <span class="sr-only">Leer después</span>
                            </a>
                        </li>
                        <li class="like-number">
                            <span class="counter">3222</span>
                            <meta itemprop="interactionCount" content="UserLikes:2"><!-- pasarle el valor que corresponda -->
                            <a href="#" class="action drive">
                                <span class="fa fa-rocket fa-lg"></span>
                                <span class="sr-only">Impulsar</span>
                            </a>
                        </li>

                        <li class="kakareo-number">
                            <span class="counter">5428</span>
                            <a href="#" class="action cluck">
                                <span class="fa fa-bullhorn fa-lg"></span>
                                <span class="sr-only">Kakarear</span>
                            </a>
                        </li>

                        <li class="more-actions">
                            <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                                <span class="fa fa-plus"></span> <span class="sr-only">Más acciones</span>
                            </span>
                        </li>
                    </ul>
                </footer>
            </article><!-- /article -->

        </li><!-- /kakareo important -->
    </ul>
</section>

</aside>
</div><!-- /.row-->
</div><!-- /.conatiner-fluid -->
</div><!-- /.row.main -->

<g:render template="/layouts/footer/footer"/>

<r:require module="tour"/>

</body>

</g:applyLayout>