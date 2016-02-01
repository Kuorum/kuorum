<!-- Header landing transparente -->
<header id="header" class="row landing ${extraHeadCss}" role="banner">
    <nav class="navbar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <g:render template="/layouts/brandAndLogo" />
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <g:render template="/layouts/searchHeadForm"/>

                <!-- Le quitamos las clases underline, etc a estos enlaces -->
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="search.html" class="navbar-link">
                            <span><g:message code="head.noLogged.search"/></span>
                        </a>
                    </li>
                    <li>
                        <a href="editors.html" class="navbar-link">
                            <span><g:message code="head.noLogged.editors"/></span>
                        </a>
                    </li>
                    <li>
                        <g:link mapping="home" class="navbar-link ${nav.activeMenuCss(mappingName: 'home')}">
                            <span><g:message code="head.noLogged.politicians"/> </span>
                        </g:link>
                    </li>
                    <li>
                        <a href="prices.html" class="navbar-link">
                            <span><g:message code="head.noLogged.prices"/></span>
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