<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | ${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/headNoLinks"/>
        <div class="row main">
            <div class="container-fluid onecol">
                <section id="main" role="main">
                    <div class="intro">
                        <g:pageProperty name="page.intro"/>
                    </div>
                    <div class="steps">
                        <ol>
                            <g:each in="${(1..4)}" var="stepNum">
                                <g:set var="actualStep" value="${Integer.parseInt(pageProperty(name: 'page.actualStep').toString())}"/>
                                <li class="${actualStep>=stepNum?'active':''}"><span class="badge">${stepNum}</span></li>
                            </g:each>
                        </ol>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 col-md-6">
                            <g:pageProperty name="page.mainContent"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-6">
                            <g:pageProperty name="page.boxes"/>
                        </div>
                    </div><!-- /.row-->
                </section>
            </div>
        </div><!-- #main -->
        <g:render template="/layouts/footer/footerRegister"/>
    </body>
</g:applyLayout>