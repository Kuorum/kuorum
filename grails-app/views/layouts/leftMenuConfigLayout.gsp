<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | ${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
        <div class="row main">
            <div class="container-fluid">
                <div class="row">
                    <section id="main" role="main" class="config ${pageProperty(name:'page.extraCssContainer')}">

                        <header id="${pageProperty(name:'page.idLeftMenu')}" class="col-xs-12 col-sm-4 col-md-4">
                            <div class="box-ppal">
                                <g:pageProperty name="page.leftMenu"/>
                            </div>
                        </header>

                        <section id="${pageProperty(name:'page.idMainContent')}" class="col-xs-12 col-sm-8 col-md-8" role="article" itemtype="http://schema.org/Article" itemscope>
                            <div class="box-ppal">
                                <g:if test="${pageProperty(name:'page.titleContent')}">
                                    <div class="box-ppal-title">
                                        <g:pageProperty name="page.titleContent"/>
                                    </div>
                                </g:if>
                                <div>
                                    <g:pageProperty name="page.mainContent"/>
                                </div>
                            </div>
                        </section>
                    </section>
                </div>
            </div>
        </div>
        <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>