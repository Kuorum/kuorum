<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>
    <body>
    <g:render template="/layouts/head"/>
    <div class="row main">
        <div class="container-fluid ${pageProperty(name: 'page.specialContainerCssClass')}">
            <div class="row">

                <!-- ********** ASIDE: COLUMNA LATERAL CON INFORMACIÓN RELACIONADA CON LA PRINCIPAL ********** -->
                <aside id="aside-ppal" class="col-md-4 col-md-push-8" role="complementary">
                    <g:pageProperty name="page.cColumn"/>
                </aside>

                <section id="main" class="col-md-8 col-md-pull-4" role="main">
                    <g:pageProperty name="page.mainContent"/>
                </section>

            </div><!-- /.row-->

        </div><!-- /.conatiner-fluid -->
    </div><!-- /.row.main -->

    <section id="otras-propuestas" class="row main">
        <div class="container-fluid">
            <g:pageProperty name="page.preFooter"/>
        </div>
    </section><!-- /#otras-propuestas -->
    <g:if test="${!Boolean.parseBoolean(pageProperty(name:'page.hideFooter')?.toString())}">
        <g:render template="/layouts/footer/footer"/>
    </g:if>
    </body>

</g:applyLayout>