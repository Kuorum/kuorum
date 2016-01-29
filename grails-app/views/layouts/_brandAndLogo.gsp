<!-- Brand and toggle get grouped for better mobile display -->
<div class="navbar-header">
    <button type="button" class="navbar-toggle bg-primary" data-toggle="collapse" data-target="#navbar-collapse">
        <span class="sr-only"><g:message code="head.navigationHelp.titleNavigation"/></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
    </button>
    <h1>
        <g:link mapping="home" class="navbar-brand ${disabledLogoLink}" elementId="brand">

            <g:set var="imageBrand" value="logo@2x.png"/>
            <nav:ifActiveMapping mappingName="home">
                <g:set var="imageBrand" value="logo-white@2x.png"/>
            </nav:ifActiveMapping>
            <img src="${resource(dir: 'images', file: imageBrand)}" alt="${g.message(code:'head.logo.alt')}">
            <span class="hidden"><g:message code="kuorum.name"/> </span>
        </g:link>
    </h1>
    <h2 class="hidden"><g:message code="head.logo.hiddenDescription"/></h2>
</div>