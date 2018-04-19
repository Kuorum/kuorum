<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | ${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
        <div class="row main">
            <g:pageProperty name="page.mainContent"/>
        </div><!-- #main -->
    <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>