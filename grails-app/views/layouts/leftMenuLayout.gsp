<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
        <div class="row main">
            <div class="container-fluid ${pageProperty(name:'page.extraCssContainer')}">

                <section id="main" role="main" class="row">

                    <header id="search-filters" class="col-xs-12 col-sm-4 col-md-3">
                        <g:pageProperty name="page.leftMenu"/>
                    </header>

                    <section id="search-results" class="col-xs-12 col-sm-8 col-md-9">
                        <g:pageProperty name="page.mainContent"/>
                    </section>
                </section>
            </div>
        </div>
        <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>