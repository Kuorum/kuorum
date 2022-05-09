<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | ${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head" model="[extraHeadCss:g.pageProperty(name:'page.extraHeadCss')]"/>
        <div class="row main">
            <div class="container-fluid">
                <div class="row">

                    <main id="main" role="main" class="sign clearfix">
                        <div class="col-sm-10 col-sm-offset-1 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1">
                            <content id="mainContent">
                                <g:pageProperty name="page.description"/>
                            </content>
                        </div>
                        <div class="col-sm-12 col-md-4">
                            <content id="description">
                                <g:pageProperty name="page.mainContent"/>
                            </content>
                        </div>
                    </main>
                </div>
            </div>
        </div><!-- #main -->
        <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>