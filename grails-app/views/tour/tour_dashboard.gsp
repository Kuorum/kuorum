<g:applyLayout name="main">

<head>
    <title><g:message code="tour.tour_dashboard.title"/></title>
</head>

<body itemscope itemtype="http://schema.org/WebPage">
    <g:render template="headTourFake" model="[user:user]"/>

    <div class="row main">

        <div class="container-fluid">
            <div class="row">
                <section id="main" class="col-xs-12 col-sm-8 col-md-8" role="main">
                    <ul id="list-kakareos-id" class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">
                        <g:each in="${fakeClucks}" var="cluck">
                            <g:render template="fakeCluck" model="[cluck:cluck]"/>
                        </g:each>
                    </ul>
                </section>

                <aside class="col-xs-12 col-sm-4 col-md-4" role="complementary">
                    <g:render template="/modules/userProfile" model="[user:user, numPosts:10]"/>
                    <g:render template="/modules/userProfileAlerts" model="[alerts:[]]"/>
                    <modulesUtil:recommendedPosts title="${message(code:"modules.recommendedPosts.title")}"/>
                    <g:render template="/modules/recommendedUsers" model="[recommendedUsers:recommendedUsers]"/>
                    <g:render template="/modules/userFavorites" model="[favorites:favorites]"/>

                </aside>
            </div><!-- /.row-->
        </div><!-- /.conatiner-fluid -->
    </div><!-- /.row.main -->

    <g:render template="/layouts/footer/footer"/>

    <r:require module="tour"/>

</body>

</g:applyLayout>