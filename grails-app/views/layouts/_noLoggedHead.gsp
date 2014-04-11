<ul class="nav navbar-nav navbar-right">
    <li class="underline">
        <g:link mapping="footerWhatIsKuorum" class="navbar-link  ${nav.activeMenuCss(mappingName: "footerWhatIsKuorum")}">
            <g:message code="head.noLogged.whatIsKuorum"/>
        </g:link>
    </li>
    <li class="underline">
        <g:link mapping="login" class="navbar-link">
            <g:message code="head.noLogged.login"/>
        </g:link>
    </li>
    <li>
        <g:link mapping="register" class="btn btn-custom-primary">
            <g:message code="head.noLogged.register"/>
        </g:link>
    </li>
</ul>