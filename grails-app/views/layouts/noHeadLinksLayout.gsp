<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/headNoLinks"/>
        <div id="main" class="row" role="main">
            <content id="mainContent">
                <g:pageProperty name="page.mainContent"/>
            </content>
        </div><!-- #main -->
        <g:render template="/layouts/footer"/>
    </body>
</g:applyLayout>