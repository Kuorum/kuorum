<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>

    <body>
        <g:render template="/layouts/head"/>
    <div class="row main">
        <div class="home">
            <section id="main" role="main">
                <g:pageProperty name="page.mainContent"/>
            </section>
        </div>
    </div><!-- #main -->

        <g:if test="${Boolean.parseBoolean(pageProperty(name:'page.preFooter').toString())}">
            <g:include controller="modules" action="registerFooterRelevantUsers"/>
        </g:if>
        <g:render template="/layouts/footer/footer"/>
        </body>
</g:applyLayout>