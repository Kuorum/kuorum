<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | ${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
        <div class="row main">
            <div class="container-fluid">
                <main id="main" role="main">
                    <g:pageProperty name="page.mainContent"/>
                </main>
            </div>
        </div>
        <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>