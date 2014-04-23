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
                    <div class="intro confirm">
                        <g:pageProperty name="page.intro"/>
                    </div>
                    <div class="max600 text-center">
                        <g:pageProperty name="page.mainContent"/>
                    </div>
                </section>
            </div>
        </div><!-- #main -->
        <g:render template="/layouts/footer/footerRegister"/>
    </body>
</g:applyLayout>