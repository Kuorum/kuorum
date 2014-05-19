<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.userNotifications"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.profileNotifications.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.profileNotifications.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileNotifications', menu:menu]"/>

</content>
<content tag="mainContent">
    <h1><g:message code="profile.profileNotifications.title"/></h1>
    <ul class="noti-filters">
        <li class="active"><a href="#">Todas</a></li>
        <li><a href="#">Alertas</a></li>
        <li><a href="#">Notificaciones</a></li>
        <li class="dropdown pull-right">
            <a data-target="#" href="" class="dropdown-toggle text-center" id="open-order" data-toggle="dropdown" role="button">Ordenar <span class="fa fa-caret-down fa-lg"></span></a>
            <ul id="ordenar" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-order" role="menu">
                <li><a href="#">Opción 1</a></li>
                <li><a href="#">Opción 2</a></li>
                <li><a href="#">Opción 3</a></li>
            </ul>
        </li>
    </ul>
    <script>
        var modalData = {}
    </script>
    <ul class="list-notification">
        <g:each in="${notifications}" var="notification">
            <g:render
                    template="/layouts/notifications/showNotification"
                    model="[notification:notification, modalUser:true, newNotification:false]"/>
        </g:each>
        %{--<li class="user">--}%
            %{--<span itemscope itemtype="http://schema.org/Person" class="popover-trigger" rel="popover" role="button" data-toggle="popover">--}%
                %{--<img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>--}%
            %{--</span>--}%
            %{--<!-- POPOVER PARA IMÁGENES USUARIOS -->--}%
            %{--<div class="popover">--}%
                %{--<a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>--}%
                %{--<div class="popover-user">--}%
                    %{--<button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>--}%
                    %{--<div class="user" itemscope itemtype="http://schema.org/Person">--}%
                        %{--<a href="#" itemprop="url">--}%
                            %{--<img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>--}%
                        %{--</a>--}%
                        %{--<span class="user-type">--}%
                            %{--<small>Activista digital</small>--}%
                        %{--</span>--}%
                    %{--</div><!-- /user -->--}%
                    %{--<button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->--}%
                    %{--<div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>--}%
                    %{--<ul class="infoActivity clearfix">--}%
                        %{--<li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>--}%
                        %{--<li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>--}%
                        %{--<li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>--}%
                        %{--<li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>--}%
                        %{--<li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>--}%
                        %{--<li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>--}%
                    %{--</ul>--}%
                %{--</div><!-- /popover-user -->--}%
            %{--</div>--}%
            %{--<!-- FIN POPOVER PARA IMÁGENES USUARIOS -->--}%
            %{--<span class="text-notification">ha abierto un debate para tu propuesta <a href="#">referendum para llegar al consenso</a></span>--}%
            %{--<span class="time">--}%
                %{--<small>--}%
                    %{--<time class="timeago" datetime="2014-04-03T14:30:17+02:00">hace 2 min</time>--}%
                %{--</small>--}%
            %{--</span>--}%
            %{--<span class="actions">--}%
                %{--<a href="#" class="btn btn-xs">Responder</a>--}%
            %{--</span>--}%
        %{--</li>--}%
        %{--<li class="user">--}%
            %{--<span itemscope itemtype="http://schema.org/Person" class="popover-trigger" rel="popover" role="button" data-toggle="popover">--}%
                %{--<img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>--}%
            %{--</span>--}%
            %{--<!-- POPOVER PARA IMÁGENES USUARIOS -->--}%
            %{--<div class="popover">--}%
                %{--<a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>--}%
                %{--<div class="popover-user">--}%
                    %{--<button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>--}%
                    %{--<div class="user" itemscope itemtype="http://schema.org/Person">--}%
                        %{--<a href="#" itemprop="url">--}%
                            %{--<img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>--}%
                        %{--</a>--}%
                        %{--<span class="user-type">--}%
                            %{--<small>Activista digital</small>--}%
                        %{--</span>--}%
                    %{--</div><!-- /user -->--}%
                    %{--<button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->--}%
                    %{--<div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>--}%
                    %{--<ul class="infoActivity clearfix">--}%
                        %{--<li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>--}%
                        %{--<li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>--}%
                        %{--<li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>--}%
                        %{--<li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>--}%
                        %{--<li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>--}%
                        %{--<li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>--}%
                    %{--</ul>--}%
                %{--</div><!-- /popover-user -->--}%
            %{--</div>--}%
            %{--<!-- FIN POPOVER PARA IMÁGENES USUARIOS -->--}%
            %{--<span class="text-notification">ha abierto un debate para tu propuesta <a href="#">referendum para llegar al consenso</a></span>--}%
            %{--<span class="time">--}%
                %{--<small>--}%
                    %{--<time class="timeago" datetime="2014-04-03T14:30:17+02:00">hace 2 min</time>--}%
                %{--</small>--}%
            %{--</span>--}%
            %{--<span class="actions">--}%
                %{--<a href="#" class="btn btn-xs" data-toggle="modal" data-target="#modalVictory">Dar victoria</a>--}%
            %{--</span>--}%



        %{--</li>--}%
        %{--<li>--}%
            %{--<span class="text-notification"><strong>Enhorabuena</strong> tu propuesta <a href="#">referendum para llegar al consenso</a> acaba de conseguir 300 impulsos</span>--}%
            %{--<span class="time">--}%
                %{--<small>--}%
                    %{--<time class="timeago" datetime="2014-04-03T14:30:17+02:00">hace 1 día</time>--}%
                %{--</small>--}%
            %{--</span>--}%
        %{--</li>--}%
        %{--<li>--}%
            %{--<span class="text-notification"><strong>Enhorabuena</strong> tu propuesta <a href="#">referendum para llegar al consenso</a> acaba de convertirse en destacada</span>--}%
            %{--<span class="time">--}%
                %{--<small>--}%
                    %{--<time class="timeago" datetime="2014-04-03T14:30:17+02:00">hace 3 días</time>--}%
                %{--</small>--}%
            %{--</span>--}%
        %{--</li>--}%
        %{--<li class="user">--}%
            %{--<span itemscope itemtype="http://schema.org/Person" class="popover-trigger" rel="popover" role="button" data-toggle="popover">--}%
                %{--<img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>--}%
            %{--</span>--}%
            %{--<!-- POPOVER PARA IMÁGENES USUARIOS -->--}%
            %{--<div class="popover">--}%
                %{--<a href="#" class="hidden" rel="nofollow">Mostrar usuario</a>--}%
                %{--<div class="popover-user">--}%
                    %{--<button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>--}%
                    %{--<div class="user" itemscope itemtype="http://schema.org/Person">--}%
                        %{--<a href="#" itemprop="url">--}%
                            %{--<img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image"><span itemprop="name">Nombre usuario</span>--}%
                        %{--</a>--}%
                        %{--<span class="user-type">--}%
                            %{--<small>Activista digital</small>--}%
                        %{--</span>--}%
                    %{--</div><!-- /user -->--}%
                    %{--<button type="button" class="btn btn-blue btn-xs allow" id="follow">Seguir</button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->--}%
                    %{--<div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>--}%
                    %{--<ul class="infoActivity clearfix">--}%
                        %{--<li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>--}%
                        %{--<li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>--}%
                        %{--<li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>--}%
                        %{--<li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>--}%
                        %{--<li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>--}%
                        %{--<li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>--}%
                    %{--</ul>--}%
                %{--</div><!-- /popover-user -->--}%
            %{--</div>--}%
            %{--<!-- FIN POPOVER PARA IMÁGENES USUARIOS -->--}%
            %{--<span class="text-notification">ha abierto un debate para tu propuesta <a href="#">referendum para llegar al consenso</a></span>--}%
            %{--<span class="time">--}%
                %{--<small>--}%
                    %{--<time class="timeago" datetime="2014-04-03T14:30:17+02:00">hace 4 días</time>--}%
                %{--</small>--}%
            %{--</span>--}%
            %{--<span class="actions">--}%
                %{--<a href="#" class="btn btn-xs">Dar victoria</a>--}%
            %{--</span>--}%
        %{--</li>--}%
    </ul>
</content>
