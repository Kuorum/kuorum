<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | ${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</title>
        <g:layoutHead/>
    </head>

    <body>
    <g:render template="/layouts/head"/>
    <div class="row main">
        <div class="container-fluid ${pageProperty(name:'page.extraCssContainer')}">
            <div class="row">
                <g:pageProperty name="page.mainContent"/>
            </div>
            <g:if test="${pageProperty(name:'page.preFooter')}">
                <div class="row">
                    <g:pageProperty name="page.preFooter"/>
                </div>
            </g:if>
        </div>
    </div><!-- #main -->

    <g:if test="${!Boolean.parseBoolean(pageProperty(name:'page.hideFooter')?.toString())}">
        <g:render template="/layouts/footer/footer"/>
    </g:if>
    </body>
</g:applyLayout>