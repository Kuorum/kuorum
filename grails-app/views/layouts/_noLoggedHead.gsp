<!-- Le quitamos las clases underline, etc a estos enlaces -->
<ul class="nav navbar-nav navbar-right no-logged">

    <li>
        <g:link mapping="landingServices" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingServices')} ${nav.activeMenuCss(mappingName: 'home')}">
            <span><g:message code="head.noLogged.services"/></span>
        </g:link>
    </li>


    <li class="dropdown">
        <a href="#" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
            <span><g:message code="head.noLogged.sectors"/> <i class="fal fa-angle-down" aria-hidden="true"></i></span>
        </a>
        <ul id="navigation-options" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-options" role="menu">
            <li>
                <g:link mapping="searcherSearchPOST" class="navbar-link ${nav.activeMenuCss(mappingName: 'searcherSearchPOST')}">
                    <span><g:message code="head.noLogged.searchPost"/></span>
                </g:link>
            </li>
            <li>
                <g:link mapping="searcherSearchDEBATE" class="navbar-link ${nav.activeMenuCss(mappingName: 'searcherSearchDEBATE')}">
                    <span><g:message code="head.noLogged.searchDebates"/></span>
                </g:link>
            </li>
            <li>
                <g:link mapping="searcherSearchSURVEY" class="navbar-link ${nav.activeMenuCss(mappingName: 'searcherSearchSURVEY')}">
                    <span><g:message code="head.noLogged.searchSurveys"/></span>
                </g:link>
            </li>
            <li>
                <g:link mapping="searcherSearchEVENT" class="navbar-link ${nav.activeMenuCss(mappingName: 'searcherSearchEVENT')}">
                    <span><g:message code="head.noLogged.searchEvents"/></span>
                </g:link>
            </li>
            <li>
                <g:link mapping="searcherSearchPARTICIPATORY_BUDGET" class="navbar-link ${nav.activeMenuCss(mappingName: 'searcherSearchPARTICIPATORY_BUDGET')}">
                    <span><g:message code="head.noLogged.searchParticipatoryBudgets"/></span>
                </g:link>
            </li>
        </ul>
    </li>

    <li>
        <g:link mapping="loginAuth" class="navbar-link ${nav.activeMenuCss(mappingName: 'loginAuth')}">
            <span><g:message code="head.noLogged.login"/></span>
        </g:link>
    </li>

    <li>
        <g:link mapping="register" class="navbar-link btn btn-transparent ${nav.activeMenuCss(mappingName: 'register')}">
            <span><g:message code="login.head.register"/></span>
        </g:link>
    </li>

    %{--<li>--}%
        %{--<g:set var="logInMapping" value="loginAuth"/>--}%
        %{--<g:set var="logInText" value="${g.message(code:"head.noLogged.login")}"/>--}%
        %{--<nav:ifActiveMapping mappingName="loginAuth">--}%
            %{--<g:set var="logInMapping" value="register"/>--}%
            %{--<g:set var="logInText" value="${g.message(code:"login.head.register")}"/>--}%
        %{--</nav:ifActiveMapping>--}%
        %{--<g:link mapping="${logInMapping}" class="navbar-link btn btn-transparent">--}%
            %{--<span>${logInText}</span>--}%
        %{--</g:link>--}%
    %{--</li>--}%
</ul>