<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/> | Kuorum.org</title>
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