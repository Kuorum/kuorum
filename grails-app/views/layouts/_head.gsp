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
    </nav>
</header>
