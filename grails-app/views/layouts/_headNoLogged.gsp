<!-- Header landing transparente -->
<header id="header" class="row landing ${extraHeadCss}" role="banner">
    <nav class="navbar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <g:render template="/layouts/brandAndLogo"/>
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <g:render template="/layouts/searchHeadForm"/>

                <!-- Le quitamos las clases underline, etc a estos enlaces -->
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="search.html" class="navbar-link">
                            <span>Search</span>
                        </a>
                    </li>
                    <li>
                        <a href="editors.html" class="navbar-link">
                            <span>Editors</span>
                        </a>
                    </li>
                    <li>
                        <a href="politicians.html" class="navbar-link ${nav.activeMenuCss(mappingName: 'politicians')}">
                            <span>Politicians</span>
                        </a>
                    </li>
                    <li>
                        <a href="prices.html" class="navbar-link">
                            <span>Prices</span>
                        </a>
                    </li>
                    <li>
                        <g:link mapping="loginAuth" class="navbar-link btn btn-transparent ${nav.activeMenuCss(mappingName: 'loginAuth')}">
                            <span><g:message code="head.noLogged.login"/></span>
                        </g:link>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>

%{--<header id="header" class="row" role="banner">--}%

    %{--<nav class="navbar navbar-fixed-top" role="navigation">--}%
        %{--<div class="container-fluid">--}%
            %{--<g:render template="/layouts/brandAndLogo"/>--}%

            %{--<!-- Collect the nav links, forms, and other content for toggling -->--}%
            %{--<div class="collapse navbar-collapse" id="navbar-collapse">--}%
                %{--<g:render template="/layouts/searchHeadForm"/>--}%
                %{--<g:render template="/layouts/discoverHead"/>--}%
                %{--<sec:ifLoggedIn>--}%
                    %{--<g:include controller="layouts" action="userHead"/>--}%
                %{--</sec:ifLoggedIn>--}%
                %{--<sec:ifNotLoggedIn>--}%
                    %{--<g:include controller="login" action="headAuth"/>--}%
                %{--</sec:ifNotLoggedIn>--}%

            %{--</div><!-- /.navbar-collapse -->--}%
        %{--</div><!-- /.container-fluid -->--}%
    %{--</nav>--}%
%{--</header>--}%