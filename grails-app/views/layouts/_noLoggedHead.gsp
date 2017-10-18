<!-- Le quitamos las clases underline, etc a estos enlaces -->
<ul class="nav navbar-nav navbar-right no-logged">
    <li>
        <g:set var="kuorumPhone" value="${g.message(code:'kuorum.telephone')}"/>
        <a href="tel:${kuorumPhone}" class="navbar-contact-phone hidden-xs">
            <span class="fa fa-phone"></span>${kuorumPhone}
        </a>
    </li>

    %{--<li>--}%
        %{--<g:link mapping="landingOrganizations" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingOrganizations')}">--}%
            %{--<span><g:message code="head.noLogged.organizations"/> </span>--}%
        %{--</g:link>--}%
    %{--</li>--}%

    %{--<li>--}%
        %{--<g:link mapping="landingCorporationsBrands" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingCorporationsBrands')}">--}%
            %{--<span><g:message code="head.noLogged.corporations"/> </span>--}%
        %{--</g:link>--}%
    %{--</li>--}%
    <li>
        <g:link mapping="landingServices" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingServices')} ${nav.activeMenuCss(mappingName: 'home')}">
            <span><g:message code="head.noLogged.services"/></span>
        </g:link>
    </li>


    <li class="dropdown" itemscope itemtype="http://schema.org/Person">
        <g:link mapping="landingOrganization" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
            <span><g:message code="head.noLogged.sectors"/> <i class="fa fa-angle-down" aria-hidden="true"></i></span>
        </g:link>
        <ul id="navigation-options" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-options" role="menu">
            <li>
                <g:link mapping="landingAdministration" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingAdministration')}">
                    <span><g:message code="head.noLogged.administration"/></span>
                </g:link>
            </li>
            <li>
                <g:link mapping="landingEnterprise" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingEnterprise')}">
                    <span><g:message code="head.noLogged.enterprise"/></span>
                </g:link>
            </li>
            <li>
                <g:link mapping="landingOrganization" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingOrganization')}">
                    <g:message code="head.noLogged.organization"/>
                </g:link>
            </li>
        </ul>
    </li>

    <li>
        <g:link mapping="landingTechnology" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingTechnology')}">
            <span><g:message code="head.noLogged.technology"/> </span>
        </g:link>
    </li>

    %{--<li>--}%
        %{--<g:link mapping="landingPrices" class="navbar-link ${nav.activeMenuCss(mappingName: 'landingPrices')}">--}%
            %{--<span><g:message code="head.noLogged.prices"/></span>--}%
        %{--</g:link>--}%
    %{--</li>--}%

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