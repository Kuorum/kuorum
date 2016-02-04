<g:applyLayout name="main">

<head>
    <title><g:layoutTitle/></title>
    <g:layoutHead/>
</head>

<body>
    <g:set var="extraHeadCss" value=""/>
    <g:set var="cssVideoAndRegister" value=""/>
    <g:ifPageProperty name="page.transparentHead" equals="true">
        <g:set var="extraHeadCss" value="transp"/>
        <g:set var="cssVideoAndRegister" value="main"/>
    </g:ifPageProperty>
    <g:render template="/layouts/head" model="[extraHeadCss:extraHeadCss]"/>

    <div class="row ${cssVideoAndRegister} landing">
        <g:pageProperty name="page.videoAndRegister"/>
    </div><!-- .main -->

    <g:if test="${pageProperty(name:"page.logos")}">
        <div class="row main special landing">
            <div class="container-fluid">
                <g:pageProperty name="page.logos"/>
            </div>
        </div>
    </g:if>

    <g:if test="${pageProperty(name:"page.special")}">
        <div class="row main special ${pageProperty(name:"page.special-cssClass")}">
            <div class="container-fluid">
                <g:pageProperty name="page.special"/>
            </div>
        </div>
    </g:if>

    <g:if test="${pageProperty(name:"page.mainContent")}">
        <div class="row main">
            <g:pageProperty name="page.mainContent"/>
        </div>
    </g:if>

    <g:if test="${pageProperty(name:"page.pressKit")}">
        <div class="row main presskit">
            <div class="container-fluid">
                <g:pageProperty name="page.pressKit"/>
            </div>
        </div>
    </g:if>

    <g:render template="/layouts/fastRegisterSection"/>
    <g:render template="/layouts/footer/footer"/>
</div><!-- .container-fluid -->
</body>
</g:applyLayout>