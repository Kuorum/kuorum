<g:applyLayout name="main">

    <head>
        <title>Kuorum.org | <g:layoutTitle/></title>
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
            <section id="services">
                <g:pageProperty name="page.howItWorks"/>
            </section>
        </div>
    </div>
    <div class="container-overflow-image row landing-section-light">
        <div class="container">
            <section class="landing-overflow-image-left" id="customWebSite">
                <g:pageProperty name="page.customWebSite"/>
            </section>
        </div>
    </div>
    <div class="container-overflow-image row landing-section-dark">
        <div class="container">
            <section class="features landing-overflow-image-right" id="features-crm">
                <g:pageProperty name="page.featuresCrm"/>
            </section>
        </div>
    </div>
    <div class="container-overflow-image row landing-section-light">
        <div class="container">
            <section class="landing-overflow-image-left features" id="features-user">
                <g:pageProperty name="page.featuresUser"/>
            </section>
        </div>
    </div>
    <div class="container-overflow-image row landing-section-dark">
        <div class="container">
            <section class="features landing-overflow-image-right" id="metrics">
                <g:pageProperty name="page.metrics"/>
            </section>
        </div>
    </div>
    <div class="container-overflow-image row landing-section-light" id="caseStudy">
        <div class="container">
            <section class="features landing-overflow-image-left" id="featured-case-study">
                <g:pageProperty name="page.caseStudy"/>
            </section>
        </div>
    </div>
    <div class="row landing-section-dark">
        <div class="container">
            <section id="statistics">
                <g:pageProperty name="page.statistics"/>
            </section>
        </div>
    </div>
    <div class="landing-section-light">
        <section id="trustUs">
            <g:pageProperty name="page.trustUs"/>
        </section>
    </div>
    <div class="row landing-section-dark">
        <div class="container">
            <section id="solutions">
                <g:pageProperty name="page.solutions"/>
            </section>
        </div>
    </div>
    <g:render template="/layouts/footer/footer"/>
    </div><!-- .container-fluid -->
    </body>
</g:applyLayout>