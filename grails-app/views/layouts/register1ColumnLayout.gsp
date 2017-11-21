<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | Kuorum.org</title>
        <g:layoutHead/>
    </head>

    <body>

    <g:render template="/layouts/head" model="[extraHeadCss:g.pageProperty(name:'page.extraHeadCss')]"/>
        <!-- IMPORTANTE este div debe estar justo después del header; no poner nada entre ellos -->
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