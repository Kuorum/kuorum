<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | Kuorum.org</title>
        <g:layoutHead/>
    </head>

    <body>
        %{--<g:render template="/layouts/headNoLinks"/>--}%
        <!-- HEADER SIN LINKS -->
    <header id="header" role="banner">
        <nav class="navbar navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <g:render template="/layouts/brandAndLogo" model="[disabledLogoLink:'disabled']"/>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="navbar-collapse">

                    <ul class="nav navbar-nav navbar-right">
                        <li class="underline" itemscope itemtype="http://schema.org/Person">
                            <a href="#" class="navbar-link user-area">
                                <span itemprop="name"><userUtil:loggedUserName/></span>
                                <strong>, <g:pageProperty name="page.headText"/></strong>
                                <img src="${image.loggedUserImgSrc()}" alt="${userUtil.loggedUserName()}" class="user-img" itemprop="image">
                            </a>
                        </li>
                        <li class="dropdown underline">
                            <a data-target="#" href="/dashboard.htm" id="open-user-options" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
                                <span class="fa fa-gear fa-lg"></span>
                                <span class="visible-xs">Opciones de usuario</span>
                            </a>
                        </li>
                    </ul>
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>

    </header>
    <!-- FIN HEADER SIN LINKS -->
        <div id="main" class="row" role="main">
            <content id="mainContent">
                <g:pageProperty name="page.mainContent"/>
            </content>
        </div><!-- #main -->
        <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>