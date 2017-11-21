<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | Kuorum.org</title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/headNoLinks"/>
        <div class="row main prices">
            <div class="container-fluid">
                <section id="payment" class="box-ppal">
                    <g:pageProperty name="page.mainContent"/>
                </section>
            </div>
        </div>
        <g:render template="/layouts/footer/footerRegister"/>
    </body>
</g:applyLayout>