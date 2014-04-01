<ul class="nav navbar-nav navbar-right">
    <li class="dropdown underline" itemscope itemtype="http://schema.org/Person">
        <a data-target="#" href="/dashboard.htm" id="open-user-options" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
            <span itemprop="name">${user.name}</span>
            <img src="images/user.jpg" alt="nombre" class="user" itemprop="image">
                <span class="fa fa-caret-down fa-lg"></span>
        </a>
        <g:render template="/layouts/headUserMenuDropDown"/>
    </li>
    <li class="dropdown underline box">
        <a data-target="#" href="/dashboard.htm" class="dropdown-toggle dropdown-menu-right navbar-link" id="open-user-messages" data-toggle="dropdown" role="button">
            <span class="fa fa-envelope fa-lg"></span> <span class="visible-xs" id="messages">Mensajes</span> <span class="badge pull-right" role="log" aria-labelledby="messages" aria-live="assertive" aria-relevant="additions">4</span>
        </a>
        <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-messages" role="menu">
            <li>Mensajes</li>
            <li>
                <span itemscope itemtype="http://schema.org/Person">
                    <img src="images/user.jpg" alt="nombre" class="user" itemprop="image"> de <a href="#" itemprop="url"><span itemprop="name">Nombre usuario</span></a>
                </span>
                <span class="fecha"><small><time pubdate datetime="2014-03-26T12:00">Hace 3 horas</time></small></span><!-- datetime hay que pasarle la fecha y hora que corresponda -->
                <span class="text-message">Aenean tempor eros ut libero hendrerit vestibulum. Nulla tincidunt malesuada dignissim. Praesent accumsan facilisis fermentum. Duis sit amet ullamcorper sem. Sed pellentesque sit amet nibh quis molestie.</span>
            </li>
            <li>
                <span itemscope itemtype="http://schema.org/Person">
                    <img src="images/user.jpg" alt="nombre" class="user" itemprop="image"> de <a href="#" itemprop="url"><span itemprop="name">Nombre usuario</span></a>
                </span>
                <span class="fecha"><small><time pubdate datetime="2014-03-26T12:00">Hace 4 horas</time></small></span><!-- datetime hay que pasarle la fecha y hora que corresponda -->
                <span class="text-message">Pellentesque congue vitae metus et malesuada. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.</span>
            </li>
            <li>
                <ul class="inline">
                    <li><small><a href="#" class="pull-left">Ver todos</a></small></li>
                    <li><small><a href="#" class="pull-right">Enviar un mensaje</a></small></li>
                </ul>
            </li>
        </ul>
    </li>
    <li class="dropdown underline box">
        <a data-target="#" href="/dashboard.htm" class="dropdown-toggle dropdown-menu-right navbar-link" id="open-user-notifications" data-toggle="dropdown" role="button">
            <span class="fa fa-bell fa-lg"></span> <span class="visible-xs" id="alerts">Alertas</span> <span class="badge pull-right" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">1</span>
        </a>
        <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-notifications" role="menu">
            <li>Notificaciones</li>
            <li>
                <span itemscope itemtype="http://schema.org/Person">
                    <img src="images/user.jpg" alt="nombre" class="user" itemprop="image"> <a href="#" itemprop="url"><span itemprop="name">Nombre usuario</span></a>
                </span>
                <span class="text-notification">aenean tempor eros ut libero hendrerit aptent taciti <a href="#">vestibulum nulla tincidunt malesuada dignissim</a></span>
                <span class="actions"><span class="pull-right"><small><a href="#">Más tarde</a></small> <button type="button" class="btn bg-ntral-dark btn-sm">Responder</button></span></span>
            </li>
            <li>
                <span itemscope itemtype="http://schema.org/Person">
                    <img src="images/user.jpg" alt="nombre" class="user" itemprop="image"> <a href="#" itemprop="url"><span itemprop="name">Nombre usuario</span></a>
                </span>
                <span class="text-notification">aenean tempor eros ut libero hendrerit aptent taciti <a href="#">vestibulum nulla tincidunt malesuada dignissim</a></span>
            </li>
            <li class="text-center"><small><a href="#">Ver todas las notificaciones</a></small></li>
        </ul>
    </li>
</ul>


%{--<sec:username/>--}%
%{--<sec:ifAnyGranted roles="ROLE_ADMIN">--}%
    %{--<g:link mapping="lawCreate" > Crear ley</g:link>--}%
%{--</sec:ifAnyGranted>--}%
