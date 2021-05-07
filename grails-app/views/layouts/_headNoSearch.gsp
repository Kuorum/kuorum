

<header id="header" class="row" role="banner">
    <nav class="navbar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <g:render template="/layouts/brandAndLogo"/>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="navbar-collapse">
                %{--<g:render template="/layouts/searchHeadForm"/>--}%
                %{--<g:render template="/layouts/discoverHead"/>--}%
                <sec:ifLoggedIn>
                    <nav:headNotifications/>
                </sec:ifLoggedIn>
                <sec:ifNotLoggedIn>
                    <g:include controller="login" action="headAuth"/>
                </sec:ifNotLoggedIn>

            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
</header>