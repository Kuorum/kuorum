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
            <div class="container-fluid onecol">
                <section id="main" role="main">
                    <div class="intro">
                        <g:pageProperty name="page.intro"/>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 col-md-6">
                            <content id="mainContent">
                                <g:pageProperty name="page.mainContent"/>
                            </content>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-6">
                            <content id="description">
                                <g:pageProperty name="page.description"/>
                            </content>
                        </div>
                    </div>
                </section>
            </div>
        </div><!-- #main -->
        <g:render template="/layouts/footer"/>
    </body>
</g:applyLayout>