<g:applyLayout name="main">
    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
        <g:pageProperty name="page.metaData"/>
    </head>
    <body>
    <g:render template="/layouts/head"/>
    <div class="row main ${pageProperty(name: 'page.specialMainRowCssClass')}">
        <div class="container-fluid ${pageProperty(name: 'page.specialContainerCssClass')}">
            <g:pageProperty name="page.intro"/>
            <div class="row" >
                <section id="main" class="${pageProperty(name: 'page.specialMainContentCssClass')} col-sm-12 col-md-8" role="main">
                    <g:pageProperty name="page.mainContent"/>
                </section>

                <aside id="aside-ppal" class="col-sm-12 col-md-4" role="complementary">
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
    <g:render template="/layouts/footer/footer"/>
    <g:if test="${pageProperty(name:'page.modals')}">
        <g:pageProperty name="page.modals"/>
    </g:if>

    </body>

</g:applyLayout>