<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
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
                            <g:pageProperty name="page.mainContent"/>
                        </section>
                    </section>
                </div>
            </div>
        </div>
        <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>