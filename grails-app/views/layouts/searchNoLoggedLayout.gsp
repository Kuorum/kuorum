<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
    <g:render template="/layouts/head" model="[extraHeadCss:'transp']"/>
    <div class="row main landing">
        <section id="main" role="main" class="landing search clearfix">
            <div class="full-video">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-sm-12">
                            <h1>FIND YOUR REPRESENTATIVES</h1>
                            <h2>Follow politicians and keep up to date about their activity.</h2>
                            <a href="#" class="btn btn-white">Sign up</a>
                            <form action="" method="post" name="findRepresentatives" id="findRepresentatives" class="form-inline searchRep" role="search">
                                <div class="form-group">
                                    <label class="sr-only" for="searchRep">Search Representatives</label>
                                    <input type="text" class="form-control" placeholder="" name="" id="searchRep" value="">
                                </div>
                                <button type="submit" class="btn btn-blue">Find my representatives</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div><!-- .main -->
    <section role="complementary" id="search-results" class="row main">
        <div class="container-fluid">
            <g:pageProperty name="page.mainContent"/>
        </div>
    </section>
    <g:render template="/layouts/fastRegisterSection"/>
    <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>