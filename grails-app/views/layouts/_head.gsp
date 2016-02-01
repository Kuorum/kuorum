<header id="header" class="row landing ${extraHeadCss}" role="banner">
    <nav class="navbar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <g:render template="/layouts/brandAndLogo" />
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <g:render template="/layouts/searchHeadForm"/>

                <sec:ifLoggedIn>
                    <g:include controller="layouts" action="userHead"/>
                </sec:ifLoggedIn>
                <sec:ifNotLoggedIn>
                    <g:include controller="login" action="headAuth"/>
                </sec:ifNotLoggedIn>
            </div>
        </div>
    </nav>
</header>
