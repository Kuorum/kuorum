<!-- Brand and toggle get grouped for better mobile display -->
<div class="navbar-header">
    <button type="button" class="navbar-toggle bg-primary" data-toggle="collapse" data-target="#navbar-collapse">
        <span class="sr-only"><g:message code="head.navigationHelp.titleNavigation"/></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
    </button>
    <g:link mapping="home" class="navbar-brand ${disableLogoLinkClass?:''}" elementId="brand">
        <domain:brandAndLogo/>
    </g:link>
    <h2 class="hidden"><g:message code="head.logo.hiddenDescription"/></h2>
</div>