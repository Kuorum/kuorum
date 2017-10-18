<%@ page import="springSecurity.KuorumRegisterCommand" %>
<header id="header" class="row ${extraHeadCss}" role="banner">
    <nav class="navbar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <g:render template="/layouts/brandAndLogo" model="[whiteLogo:extraHeadCss?.contains('transp')]"/>
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <g:render template="/layouts/searchHeadForm"/>
                <nav:ifPageProperty pageProperty="showNavBar">
                    <sec:ifLoggedIn>
                        <g:include controller="layouts" action="userHead"/>
                    </sec:ifLoggedIn>
                    <sec:ifNotLoggedIn>
                        %{--<g:include controller="login" action="headAuth"/>--}%
                        <g:render template="/layouts/noLoggedHead" model="[registerCommand: new springSecurity.KuorumRegisterCommand()]"/>
                    </sec:ifNotLoggedIn>
                </nav:ifPageProperty>
            </div>
        </div>
        <sec:ifNotLoggedIn>
            <div class="navbar-contact-phone hidden-md hidden-lg">
                <g:set var="kuorumPhone" value="${g.message(code:'kuorum.telephone')}"/>
                <span class="fa fa-phone"></span>
                <a href="tel:${kuorumPhone}">${kuorumPhone}</a>
            </div>
        </sec:ifNotLoggedIn>
    </nav>
</header>
