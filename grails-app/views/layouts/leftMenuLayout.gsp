<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
        <div class="row main">
            <div class="container-fluid search">

                <section id="main" role="main" class="row">

                    <header id="search-filters" class="col-xs-12 col-sm-4 col-md-4">
                        <g:pageProperty name="page.leftMenu"/>
                    </header>

                    <section id="search-results" class="col-xs-12 col-sm-8 col-md-8">
                        <g:pageProperty name="page.mainContent"/>
                    </section>
                </section>
            </div>
        </div>
        <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>