<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
        <div class="row main">
            <div class="container-fluid">
                <section id="main" role="main">
                    <g:pageProperty name="page.mainContent"/>
                </section>
                <g:render template="/layouts/footer/footer"/>
            </div>
        </div>
    </body>
</g:applyLayout>