<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${law.shortName}</title>
    <meta name="layout" content="columnCLayout">
</head>


<content tag="mainContent">
    <article class="kakareo post ley" role="article" itemscope itemtype="http://schema.org/Article">
        <div class="callMobile"><a href="#vote" class="smooth">¡Vota!</a></div>
        <g:if test="${law.image}">
            <g:render template="lawPhoto" model="[law:law]"/>
        </g:if>
        <div itemprop="keywords" class="laley">${law.hashtag}</div>
        <h1>${law.shortName}</h1>

        <div class="row options">
            <div class="col-xs-12 col-sm-6 col-md-6 editPost">
                <!-- dejar la estructura aunque a veces esté vacío  -->
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6 leerLey">
                <a href="${law.urlPdf}" target="_blank">Leer ley completa</a>
            </div>
        </div>

        <p class="cl-ntral-dark">${law.realName}</p>
        <p>Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend reformidans ei. Omnium euismod in pri, eam ei oblique numquam dignissim, vel et erant nostro suscipiantur.</p>
        <p>Consul legendos expetendis id vis. Ne saperet civibus rationibus has. Enim semper maiestatis no quo, ius at erat tollit. Adhuc epicuri intellegam te est. Solet animal apeirian eu est, malis legendos dissentias te usu. Vim at sint meliore officiis, mucius epicurei vel et. Duo fastidii quaestio an. Vim at veri eripuit fabellas, prompta docendi ea mei.</p>
        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis en</p>

        <div class="readMore"><a data-toggle="collapse" data-parent="#accordion" href="#collapse" class="collapsed">Leer más<span class="fa fa-chevron-circle-down fa-lg"></span></a></div>
        <div id="collapse" class="panel-collapse collapse">
            Mucius epicurei vel et. Duo fastidii quaestio an. Consul legendos expetendis id vis. Ne saperet civibus rationibus has. Enim semper maiestatis no quo, ius at erat tollit. Adhuc epicuri intellegam te est. Solet animal apeirian eu est, malis legendos dissentias te usu. Vim at sint meliore officiis, mucius epicurei vel et. Duo fastidii quaestio an. Vim at veri eripuit fabellas, prompta docendi ea mei.
        </div>
    </article><!-- /article -->

    <aside class="participate">
        <h1>Participa expresando tu voz</h1>
        <p>Crea una publicación sobre algún artículo en concreto de la ley.</p>
        <div class="row">
            <div class="col-xs-12 col-sm-4 col-md-4">
                <a href="#"><span class="icon-notebook26 fa-2x"></span><br>Historia</a>
                <p>Seguro que sabes mucho del tema. Que tienes ideas para mejorarla. Publica tu idea y si es de las mas apoyadas podremos cambiar la ley.</P>
            </div>
            <div class="col-xs-12 col-sm-4 col-md-4">
                <a href="#"><span class="fa fa-question-circle fa-2x"></span><br>Pregunta</a>
                <p>Cuéntanos tu historia. En qué te afecta esta ley. Nos comprometemos a hacerla llegar a los políticos para que la tengan en cuenta.</p>
            </div>
            <div class="col-xs-12 col-sm-4 col-md-4">
                <a href="#"><span class="fa fa-lightbulb-o fa-2x"></span><br>Propuesta</a>
                <p>Pregunta tus dudas sobre la ley. En qué te afectará. Y los políticos podrán verla. Responder y hasta llevarla a al congreso.</p>
            </div>
        </div>
    </aside>

    <aside class="seeVictory" id="seeVictory">
        <h1>Mira lo que otros han conseguido</h1>
        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</p>
        <strong>¡Hemos conseguido <span class="counter">3</span> victorias!</strong>

        <div class="controls">
            <a href="#victory" data-slide="prev">
                <span class="fa-stack fa-lg">
                    <i class="fa fa-square fa-stack-2x"></i>
                    <i class="fa fa-caret-left fa-stack-1x fa-inverse"></i>
                </span>
            </a>
            <span class="indexCounter"><span class='carousel-index'><span class="actual"></span> de <span class="total"></span></span> victorias</span> <!-- aquí se genere al índice por js-->
            <a href="#victory" data-slide="next">
                <span class="fa-stack fa-lg">
                    <i class="fa fa-square fa-stack-2x"></i>
                    <i class="fa fa-caret-right fa-stack-1x fa-inverse"></i>
                </span>
            </a>
        </div>

        <div id="victory" class="carousel slide">
            <div class="carousel-inner">

                <div class="item">
                    <div class="sliderItem clearfix">
                        <h1>¡<a href="#">Fulanito de tal</a> lo consiguió! Tú también puedes hacerlo.</h1>
                        <img alt="texto-alternativo-imagen" src="images/photo.jpg" class="actor">
                        <div class="content">
                            <p>Etiam vel lacus sed velit fringilla porta in id tortor. Donec libero eros, tristique id scelerisque eu, lobortis et est. Nlacus sed velitsas</p> <!-- 140 caracteres -->
                            <p><strong>y 3 días después</strong></p>
                            <div class="user" itemscope itemtype="http://schema.org/Person">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"> <span itemprop="name">Nombre usuario</span> se comprometió a llevarla al congreso
                            </div>
                            <a href="#">Conoce toda la historia</a>
                        </div>
                    </div>
                </div> <!-- slider 1 -->

                <div class="item">
                    <div class="sliderItem clearfix">
                        <h1>¡<a href="#">Menganito de tal</a> lo consiguió! Tú también puedes hacerlo.</h1>
                        <img alt="texto-alternativo-imagen" src="images/photo.jpg" class="actor">
                        <div class="content">
                            <p>Etiam vel lacus sed velit fringilla porta in id tortor. Donec libero eros, tristique id scelerisque eu, lobortis et est. Nlacus sed velitsas</p> <!-- 140 caracteres -->
                            <p><strong>y 3 días después</strong></p>
                            <div class="user" itemscope itemtype="http://schema.org/Person">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"> <span itemprop="name">Nombre usuario</span> se comprometió a llevarla al congreso
                            </div>
                            <a href="#">Conoce toda la historia</a>
                        </div>
                    </div>
                </div> <!-- slider 2 -->

                <div class="item">
                    <div class="sliderItem clearfix">
                        <h1>¡<a href="#">Tercero de la lista</a> lo consiguió! Tú también puedes hacerlo.</h1>
                        <img alt="texto-alternativo-imagen" src="images/photo.jpg" class="actor">
                        <div class="content">
                            <p>Etiam vel lacus sed velit fringilla porta in id tortor. Donec libero eros, tristique id scelerisque eu, lobortis et est. Nlacus sed velitsas</p> <!-- 140 caracteres -->
                            <p><strong>y 3 días después</strong></p>
                            <div class="user" itemscope itemtype="http://schema.org/Person">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"> <span itemprop="name">Nombre usuario</span> se comprometió a llevarla al congreso
                            </div>
                            <a href="#">Conoce toda la historia</a>
                        </div>
                    </div>
                </div> <!-- slider 3 -->

            </div><!-- carousel inner -->

        </div><!-- carousel -->
    </aside>
    <aside class="participAll">
        <h1>Ellos participaron</h1>
        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</p>
        <!-- COMIENZA LISTA DE KAKAREOS -->
        <ul class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">
            <g:each in="${clucks}" var="cluck">
                <g:render template="/cluck/cluck" model="[cluck:cluck]"/>
            </g:each>
        </ul>
    </aside>
</content>

<content tag="cColumn">
<section class="boxes vote" id="vote">
    <h1>Tú también tienes voz... y voto</h1>
    <p>Siempre has pensado que no se puede hacer nada, que todo está muy mal... pues ahora es el momento de participar.</p>
    <ul class="activity">
        <li class="favor"><span>1232</span> A favor</li>
        <li class="contra"><span>2422</span> En contra</li>
        <li class="abstencion"><span>322</span> Abstención</li>
    </ul>
    <div class="kuorum">
        Faltan <span class="counter">35</span> votos para kuorum
        <span class="popover-trigger fa fa-info-circle" data-toggle="popover" rel="popover" role="button"></span>
        <!-- POPOVER KUORUM -->
        <div class="popover">
            <a href="#" class="hidden" rel="nofollow">Mostrar lista de usuarios</a>
            <div class="popover-kuorum">

                <p class="text-center">¿Qué ocurre si hay <em>kuorum</em>?</p>
                <p>Texto que explica el kuorum. Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend reformidans ei. Omnium euismod in pri, eam ei oblique numquam dignissim, vel et erant nostro suscipiantur.</p>
            </div><!-- /popover-more-kuorum -->
        </div>
        <!-- FIN POPOVER KUORUM -->
    </div>
    <div class="voting">
        <!-- LOGADO NO VOTADO -->
        <ul>
            <li><a href="#" class="btn btn-blue yes"><span class="icon-smiley fa-2x"></span> A favor</a></li>
            <li><a href="#" class="btn btn-blue no"><span class="icon-sad fa-2x"></span> En contra</a></li>
            <li><a href="#" class="btn btn-blue neutral"><span class="icon-neutral fa-2x"></span> Abstención</a></li>
        </ul>

        <!-- LOGADO VOTADO -->
        <a href="#" class="changeOpinion">¿Has cambiado de opinión?</a>

        <!-- NO LOGADO NO VOTADO -->
        <!-- <a href="#" class="btn btn-blue btn-block vote">Vota <br> <small>Es tu momento de hablar</small></a> --> <!-- al hacer click lo deshabilito y cambio el texto -->

        <a href="#">Ficha técnica</a>
    </div>

    <div class="social">
        <p>Comparte en las redes sociales</p>
        <ul class="social">
            <li><a href="#"><span class="sr-only">Twitter</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-twitter fa-stack-1x"></span></span></a></li>
            <li><a href="#"><span class="sr-only">Facebook</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-facebook fa-stack-1x"></span></span></a></li>
            <li><a href="#"><span class="sr-only">LinkedIn</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-linkedin fa-stack-1x"></span></span></a></li>
            <li><a href="#"><span class="sr-only">Google+</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-google-plus fa-stack-1x"></span></span></a></li>
        </ul>
    </div>
</section>

<section class="boxes follow">
    <h1>Participando en esta ley</h1>
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

<section class="boxes guay">
    <h1>Publicaciones destacadas</h1>
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
                            <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                                <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                            </span>
                            <!-- POPOVER PARA IMÁGENES USUARIOS -->
                            <div class="popover">

                                <a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>
                                <div class="popover-user">
                                    <div class="user" itemscope itemtype="http://schema.org/Person">
                                        <a href="#" itemprop="url">
                                            <img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>
                                        </a>
                                        <span class="user-type">
                                            <small>Activista digital</small>
                                        </span>
                                    </div><!-- /user -->
                                    <button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
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
                            <time class="timeago" datetime="2014-04-07T13:40:50+02:00">hace 12 días</time>
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
                            <time class="timeago" datetime="2014-04-07T13:40:50+02:00">hace 3 horas</time>
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
                            <a href="#" class="action cluck"><span class="fa fa-bullhorn fa-lg"></span><span class="sr-only">Kakarear</span></a>
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
</content>

<content tag="footerStats">
    <ul>
        <li><span class="fa fa-heart fa-lg"></span> 3 propuestas han alcanzado los 1.000 apoyos.</li>
        <li><span class="icon-clock2 fa-lg"></span> última actividad sobre la ley hace 3 días.</li>
        <li><span class="icon-eye fa-lg"></span> 3.228 vistas de esta ley.</li>
        <li><span class="icon-users fa-lg"></span> 54 personas actuando sobre la ley.</li>
    </ul>
    <a href="#main" class="smooth top"><span class="fa fa-caret-up fa-lg"></span>Arriba</a>
</content>
