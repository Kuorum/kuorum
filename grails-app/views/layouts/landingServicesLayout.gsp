<g:applyLayout name="main">

    <head>
        <title> ${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name} | <g:layoutTitle/></title>
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
            <section id="how-it-works">
                <g:pageProperty name="page.howItWorks"/>
            </section>
        </div>
    </div>

<g:if test="${g.pageProperty(name:'page.showLatestActivities')=="true"}">
    <div class="row landing-section-light">
        <div class="container">
            <section id="latestActivities">
                <g:pageProperty name="page.latestActivities"/>
            </section>
        </div>
    </div>
</g:if>

    <g:render template="/layouts/footer/footer"/>
    </div><!-- .container-fluid -->
    </body>
</g:applyLayout>