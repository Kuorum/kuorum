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
                        <g:link mapping="searcherSearch" class="navbar-link">
                            <span><g:message code="head.noLogged.search"/></span>
                        </g:link>
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
                        <g:link mapping="landingPrices" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingPrices')}">
                            <span><g:message code="head.noLogged.prices"/></span>
                        </g:link>
                    </li>
                    <li>
                        <g:set var="logInMapping" value="register"/>
                        <g:set var="logInText" value="${g.message(code:"login.head.register")}"/>
                        <nav:ifActiveMapping mappingName="register">
                            <g:set var="logInMapping" value="loginAuth"/>
                            <g:set var="logInText" value="${g.message(code:"head.noLogged.login")}"/>
                        </nav:ifActiveMapping>
                        <g:link mapping="${logInMapping}" class="navbar-link btn btn-transparent">
                            <span>${logInText}</span>
                        </g:link>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>