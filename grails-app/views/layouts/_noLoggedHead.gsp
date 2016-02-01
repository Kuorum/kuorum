<%@ page import="kuorum.core.model.solr.SolrType" %>
<!-- Le quitamos las clases underline, etc a estos enlaces -->
<ul class="nav navbar-nav navbar-right">
    <li>
        <g:link mapping="searcherSearch" class="navbar-link ${nav.activeMenuCss(mappingName: 'searcherSearch')}" params="[type:SolrType.POLITICIAN]">
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
        <a href="prices.html" class="navbar-link">
            <span><g:message code="head.noLogged.prices"/></span>
        </a>
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