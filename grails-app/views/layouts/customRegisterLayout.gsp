<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>

        <header id="header" class="row" role="banner">
            <nav class="navbar navbar-fixed-top" role="navigation">
                <div class="container-fluid">
                    <g:render template="/layouts/brandAndLogo"/>

                    <!-- Collect the nav links, forms, and other content for toggling -->
                    <!-- Collect the nav links, forms, and other content for toggling -->
                    <div class="collapse navbar-collapse" id="navbar-collapse">
                        <ul class="nav navbar-nav navbar-right">
                            <li>
                                <span class="navbar-link user-area">
                                    <span>Nombre usuario</span> <img src="images/pre-user.jpg" alt="nombre" class="user-img">
                                </span>
                            </li>
                            <li>
                                <span id="open-user-options" class="navbar-link">
                                    <span class="fa fa-gear fa-lg"></span>
                                    <span class="visible-xs">Opciones de usuario</span>
                                </span>

                            </li>
                            <li>
                                <span class="navbar-link" id="open-user-messages">
                                    <span class="fa fa-envelope fa-lg"></span>
                                    <span class="visible-xs" id="messages">Mensajes</span>
                                </span>

                            </li>
                            <li>
                                <span class="navbar-link" id="open-user-notifications">
                                    <span class="fa fa-bell fa-lg"></span>
                                    <span class="visible-xs" id="alerts">Notificaciones</span>
                                </span>

                            </li>
                        </ul>
                    </div><!-- /.navbar-collapse -->
                </div><!-- /.container-fluid -->
            </nav>
        </header>
        <div class="row main">
            <div class="container-fluid onecol">
                <section id="main" role="main">
                    <div class="intro">
                        <g:pageProperty name="page.intro"/>
                    </div>
                    <div class="steps">
                        <ol>
                            <g:each in="${(1..4)}" var="stepNum">
                                <g:set var="actualStep" value="${Integer.parseInt(pageProperty(name: 'page.actualStep').toString())}"/>
                                <li class="${actualStep>=stepNum?'active':''}"><span class="badge">${stepNum}</span></li>
                            </g:each>
                        </ol>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 col-md-6">
                            <g:pageProperty name="page.mainContent"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-6">
                            <g:pageProperty name="page.boxes"/>
                        </div>
                    </div><!-- /.row-->
                </section>
            </div>
        </div><!-- #main -->
        <g:render template="/layouts/footer/footerRegister"/>
    </body>
</g:applyLayout>