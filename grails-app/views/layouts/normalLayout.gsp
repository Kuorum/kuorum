<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
        <div id="main" class="row" role="main">
            <content id="mainContent">
                <g:pageProperty name="page.mainContent"/>
            </content>
        </div><!-- #main -->
        <g:render template="/layouts/footer/footer"/>
    </body>
</g:applyLayout>