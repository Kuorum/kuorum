<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | ${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</title>
        <g:layoutHead/>
    </head>

    <body>
    <g:render template="/layouts/headNoSearch"/>
    <div class="row main ${pageProperty(name:'page.extraCssContainer')}"">
    <div class="container-fluid">
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
        <g:render template="/layouts/footer/footerNoScape"/>
    </g:if>
    </body>
</g:applyLayout>