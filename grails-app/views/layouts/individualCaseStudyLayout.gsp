<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
        <parameter name="bodyCss" value="landing-2"/>
    </head>

    <body>
    <g:render template="/layouts/head" model="[extraHeadCss:g.pageProperty(name:'page.extraHeadCss')]"/>

    <div class="row main landing">
        <section id="main" role="main" class="landing clearfix">
            <g:pageProperty name="page.main"/>
        </section>
    </div>
    <div class="row landing-section-light">
        <div class="container">
            <section id="cases-study-body">
                <div class="section-body">
                    <div class="breadcrumbs">
                        <g:pageProperty name="page.breadcrumb"/>
                    </div>
                    <div class="content col-md-10 col-md-offset-1">
                        <div class="article">
                            <g:pageProperty name="page.caseStudyBody"/>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
    <div class="row landing-section-dark">
        <div class="container">
            <section id="cases-study-grid">
                <g:pageProperty name="page.casesStudyGrid"/>
            </section>
        </div>
    </div>
    <g:render template="/layouts/footer/footer"/>
    </div><!-- .container-fluid -->
    </body>
</g:applyLayout>