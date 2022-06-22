<!-- Le quitamos las clases underline, etc a estos enlaces -->
<ul class="nav navbar-nav navbar-right no-logged">

    <li>
        <g:link mapping="landingServices"
                class="navbar-link ${nav.activeMenuCss(mappingName: 'landingServices')}${nav.activeMenuCss(mappingName: 'home')}">
            <span><g:message code="head.noLogged.services"/></span>
        </g:link>
    </li>


    <nav:onlyPublicDomain>
       <g:if test="${_domainActiveCampaigns.size() == 8}">
           <g:set var="activeCampaign" value="${_domainActiveCampaigns.get(0)}"/>
            <li>
                <g:link mapping="searcherSearch${activeCampaign}"
                        class="navbar-link ${nav.activeMenuCss(mappingName: 'searcherSearch' + activeCampaign)}">
                    <span><g:message code="head.noLogged.sectors"/></span>
                </g:link>
            </li>
        </g:if>
        <g:else>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown"
                   role="button">
                    <span><g:message code="head.noLogged.sectors"/> <i class="fal fa-angle-down" aria-hidden="true"></i>
                    </span>
                </a>
                <ul id="navigation-options" class="dropdown-menu dropdown-menu-right"
                    aria-labelledby="open-user-options" role="menu">
                    <g:each in="${_domainActiveCampaigns}" var="activeSolrType">
                        <li>
                            <g:link mapping="searcherSearch${activeSolrType}"
                                    class="navbar-link ${nav.activeMenuCss(mappingName: 'searcherSearch' + activeSolrType)}">
                                <span><g:message code="search.filters.SolrType.${activeSolrType}"/></span>
                            </g:link>
                        </li>
                    </g:each>
                </ul>
            </li>
        </g:else>
    </nav:onlyPublicDomain>

    <li>
        <g:link mapping="loginAuth" class="navbar-link ${nav.activeMenuCss(mappingName: 'loginAuth')}">
            <span><g:message code="head.noLogged.login"/></span>
        </g:link>
    </li>

    <li>
        <g:link mapping="register"
                class="navbar-link btn btn-transparent ${nav.activeMenuCss(mappingName: 'register')}">
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