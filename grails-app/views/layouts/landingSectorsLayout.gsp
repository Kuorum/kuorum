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
    <div class="row landing-section-dark">
        <div class="container">
            <section id="pain">
                <g:pageProperty name="page.pain"/>
            </section>
        </div>
    </div>
    <div class="row landing-section-dark">
        <div class="container">
            <section class="features" id="gain">
                <g:pageProperty name="page.gain"/>
            </section>
        </div>
    </div>
    <div class="container-overflow-image row landing-section-light">
        <div class="container">
            <section class="features" id="featured-case-study">
                <g:pageProperty name="page.caseStudy"/>
            </section>
        </div>
    </div>
    <div class="row landing-section-dark">
        <div class="container">
            <section id="contact-us" class="col-md-10 col-md-offset-1">
                <g:pageProperty name="page.contactUs"/>
            </section>
        </div>
    </div>
    <g:render template="/layouts/footer/footer"/>
    </div><!-- .container-fluid -->
    </body>
</g:applyLayout>