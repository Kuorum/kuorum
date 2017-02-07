<g:applyLayout name="main">

<head>
    <title><g:layoutTitle/></title>
    <g:layoutHead/>
</head>

<body>
    <g:render template="/layouts/head" model="[extraHeadCss:'']"/>

    <div class="row main landing">
        <section id="main" role="main" class="landing clearfix">
            <g:pageProperty name="page.main"/>
        </section>
    </div>
    <div class="light" class="row main">
        <div class="container">
            <section id="how-it-works">
                <g:pageProperty name="page.howItWorks"/>
            </section>
        </div>
    </div>
    <div class="container-overflow-image row main">
        <div class="container">
            <section class="row" id="engage">
                <g:pageProperty name="page.engage"/>
            </section>
        </div>
    </div>
    <div class="light">
        <div class="container">
            <section id="organizations">
                <g:pageProperty name="page.organizations"/>
            </section>
        </div>
    </div>
    <div id="case-study" class="row main">
        <div class="image">
            <div class="overlay"></div>
        </div>
        <div class="content container">
            <div class="col-md-8 col-md-offset-5">
                <g:pageProperty name="page.caseStudy"/>
            </div>
        </div>
    </div>
    <div class="container-overflow-image row main">
        <div class="container">
            <section id="progress">
                <g:pageProperty name="page.features"/>
            </section>
        </div>
    </div>
    <g:render template="/layouts/footer/footer"/>
</div><!-- .container-fluid -->
</body>
</g:applyLayout>