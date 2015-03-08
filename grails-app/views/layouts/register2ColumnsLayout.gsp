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
                    <div class="collapse navbar-collapse" id="navbar-collapse">
                    <content id="headButtons">
                        <g:pageProperty name="page.headButtons"/>
                    </content>
                    </div><!-- /.navbar-collapse -->
                </div><!-- /.container-fluid -->
            </nav>
        </header>
        <div class="row main">
            <div class="container-fluid">
                <div class="row">

                    <section id="main" role="main" class="sign clearfix">
                        <div class="col-sm-10 col-sm-offset-1 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1">
                            <content id="mainContent">
                                <g:pageProperty name="page.description"/>
                            </content>
                        </div>
                        <div class="col-sm-12 col-md-4">
                            <content id="description">
                                <g:pageProperty name="page.mainContent"/>
                            </content>
                        </div>
                    </section>
                </div>
            </div>
        </div><!-- #main -->
        %{--<g:render template="/layouts/footer/footerNoScape"/>--}%
    </body>
</g:applyLayout>