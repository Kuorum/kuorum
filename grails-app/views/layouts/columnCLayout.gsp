<g:applyLayout name="main">
    <g:pageProperty name="page.metaData"/>
    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>
    <body>
    <g:render template="/layouts/head"/>
    <g:if test="${pageProperty(name:'page.subHeader')}">
        <section id="info-sup-scroll">
            <div class="container-fluid">
                <div class="row">
                    <g:pageProperty name="page.subHeader"/>
                </div>
            </div>
        </section>
    </g:if>
    <div class="row main">
        <div class="container-fluid ${pageProperty(name: 'page.specialContainerCssClass')}">
            <g:pageProperty name="page.intro"/>
            <div class="row" >
                <section id="main" class="col-sm-12 col-md-8" role="main">
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
            <g:if test="${pageProperty(name:'page.extraRowData')}">
                <div class="row">
                    <div class="hidden-xs col-sm-12">
                        <g:pageProperty name="page.extraRowData"/>
                    </div>
                </div>
            </g:if>
        </div><!-- /.conatiner-fluid -->
    </div><!-- /#main -->
    <g:if test="${pageProperty(name:'page.preFooterSections')}">
        <g:pageProperty name="page.preFooterSections"/>
    </g:if>
    <g:if test="${!Boolean.parseBoolean(pageProperty(name:'page.hideFooter')?.toString())}">
        <g:render template="/layouts/footer/footer"/>
    </g:if>
    </body>

</g:applyLayout>