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
            <img src="${resource(dir: 'images', file: 'logo@2x.png')}" alt="${g.message(code:'head.logo.alt')}">
            <span class="hidden"><g:message code="kuorum.name"/> </span>
        </g:link>
    </h1>
    <h2 class="hidden"><g:message code="head.logo.hiddenDescription"/></h2>
</div>