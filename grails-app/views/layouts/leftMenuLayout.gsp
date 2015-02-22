<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
        <div class="row main">
            <div class="container-fluid ${pageProperty(name:'page.extraCssContainer')}">
                <div class="row">
                    <section id="main" role="main" class="config">

                        <header id="${pageProperty(name:'page.idLeftMenu')}" class="col-xs-12 col-sm-4 col-md-4">
                            <div class="box-ppal">
                                <g:pageProperty name="page.leftMenu"/>
                            </div>
                        </header>

                        <section id="${pageProperty(name:'page.idMainContent')}" class="col-xs-12 col-sm-8 col-md-8">
                            <g:pageProperty name="page.mainContent"/>
                        </section>
                    </section>
                </div>
            </div>
        </div>
        <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>