<sec:ifNotLoggedIn>
    <g:set var="extraHeadCss" value="landing ${extraHeadCss}"/>
</sec:ifNotLoggedIn>

<header id="header" class="row landing ${extraHeadCss}" role="banner">
    <nav class="navbar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <g:render template="/layouts/brandAndLogo" model="[whiteLogo:extraHeadCss?.contains('transp')]"/>
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <g:render template="/layouts/searchHeadForm"/>
                <nav:ifPageProperty pageProperty="showNavBar">
                    <sec:ifLoggedIn>
                        <g:include controller="layouts" action="userHead"/>
                    </sec:ifLoggedIn>
                    <sec:ifNotLoggedIn>
                        <g:include controller="login" action="headAuth"/>
                    </sec:ifNotLoggedIn>
                </nav:ifPageProperty>
            </div>
        </div>
    </nav>
</header>
