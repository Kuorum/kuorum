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
    <div class="row light">
        <div class="landing-footer">
            <div class="left-container col-md-3">
                <g:pageProperty name="page.footerLeftColumn"/>
            </div>
            <div class="col-md-9">
                <section id="footer-section">
                    <g:pageProperty name="page.footerSection"/>
                </section>
            </div>
        </div>
    </div>
    <g:render template="/layouts/footer/footer"/>
    </div><!-- .container-fluid -->
    </body>
</g:applyLayout>