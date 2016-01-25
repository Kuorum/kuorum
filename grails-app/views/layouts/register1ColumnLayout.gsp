<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>

        <g:render template="/layouts/headNoLogged"/>
        <div class="row main">
            <div class="container-fluid onecol">
                <section id="main" role="main" class="homeSub">
                    <h1><g:pageProperty name="page.title"/></h1>
                    <g:pageProperty name="page.mainContent"/>
                </section>
            </div>
        </div><!-- .main -->
        <g:render template="/layouts/footer/footerRegister"/>
    </body>
</g:applyLayout>