<!-- Brand and toggle get grouped for better mobile display -->
<div class="navbar-header">
    <button type="button" class="navbar-toggle bg-primary" data-toggle="collapse" data-target="#navbar-collapse">
        <span class="sr-only"><g:message code="head.navigationHelp.titleNavigation"/></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
    </button>
    %{--<h1>--}%
        <g:link mapping="home" class="navbar-brand ${disabledLogoLink}" elementId="brand">

            <g:if test="${request.forwardURI.contains("/ignaciogp")}">
                <g:set var="imageBrand" value="logo-junta@2x.png"/>
            </g:if>
            <g:else>
                <g:set var="imageBrand" value="logo@2x.png"/>
                <g:if test="${whiteLogo}">
                    <g:set var="imageBrand" value="logo-white@2x.png"/>
                </g:if>
            </g:else>
            <img src="${resource(dir: 'images', file: imageBrand)}" alt="${g.message(code:'head.logo.alt')}" title="${g.message(code:'head.logo.title')}">
            <span class="hidden"><g:message code="kuorum.name"/> </span>
        </g:link>
    %{--</h1>--}%
    <h2 class="hidden"><g:message code="head.logo.hiddenDescription"/></h2>
</div>