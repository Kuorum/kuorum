<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>
    <body>
    <g:render template="/layouts/head"/>
    <div class="row main">
        <div class="container-fluid ${pageProperty(name:'page.extraCssContainer')}">
            <g:pageProperty name="page.intro"/>
            <div class="row" >
                <section id="main" class="col-xs-12 col-sm-8 col-md-8" role="main">
                    <g:pageProperty name="page.mainContent"/>
                </section>

                <aside class="col-xs-12 col-sm-4 col-md-4" role="complementary">
                    <g:pageProperty name="page.cColumn"/>
                </aside>
            </div>
        </div><!-- /.conatiner-fluid -->
    </div><!-- /#main -->
    <g:render template="/layouts/footer/footer"/>
    </body>

</g:applyLayout>