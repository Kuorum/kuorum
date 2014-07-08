<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>
    <body>
    <g:render template="/layouts/headNoLinks"/>
    <div class="row main">
        <div class="container-fluid ${pageProperty(name: 'page.specialContainerCssClass')}">
            <g:pageProperty name="page.intro"/>
            <div class="row" >
                <section id="main" class="col-xs-12 col-sm-12 col-md-8" role="main">
                    <g:pageProperty name="page.mainContent"/>
                </section>

                <aside class="col-xs-12 col-sm-12 col-md-4" role="complementary">
                    <g:pageProperty name="page.cColumn"/>
                </aside>

                <g:if test="${pageProperty(name:'page.footerStats')}">
                    <div class="stats col-xs-12 col-sm-4 col-md-4">
                        <g:pageProperty name="page.footerStats"/>
                    </div>
                </g:if>
            </div>
        </div><!-- /.conatiner-fluid -->
    </div><!-- /#main -->
    <g:render template="/layouts/footer/footerRegister"/>
    </body>

</g:applyLayout>