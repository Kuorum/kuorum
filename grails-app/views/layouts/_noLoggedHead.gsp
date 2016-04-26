<!-- Le quitamos las clases underline, etc a estos enlaces -->
<ul class="nav navbar-nav navbar-right">
    <li>
        <g:link mapping="searcherLanding" class="navbar-link ${nav.activeMenuCss(mappingName: 'searcherLanding')} ${nav.activeMenuCss(mappingName: 'home')}">
            <span><g:message code="head.noLogged.search"/></span>
        </g:link>
    </li>
    <li>
        <g:link mapping="landingEditors" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingEditors')}">
            <span><g:message code="head.noLogged.editors"/></span>
        </g:link>
    </li>
    <li>
        <g:link mapping="landingPoliticians" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingPoliticians')}">
            <span><g:message code="head.noLogged.politicians"/> </span>
        </g:link>
    </li>
    <li>
        <g:link mapping="landingPrices" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingPrices')}">
            <span><g:message code="head.noLogged.prices"/></span>
        </g:link>
    </li>
    <li>
        <g:set var="logInMapping" value="loginAuth"/>
        <g:set var="logInText" value="${g.message(code:"head.noLogged.login")}"/>
        <nav:ifActiveMapping mappingName="loginAuth">
            <g:set var="logInMapping" value="register"/>
            <g:set var="logInText" value="${g.message(code:"login.head.register")}"/>
        </nav:ifActiveMapping>
        <g:link mapping="${logInMapping}" class="navbar-link btn btn-transparent">
            <span>${logInText}</span>
        </g:link>
    </li>
</ul>