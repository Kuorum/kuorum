<g:applyLayout name="main">

<head>
    <title><g:layoutTitle/></title>
    <g:layoutHead/>
</head>

<body>
    <g:render template="/layouts/head" model="[extraHeadCss:'transp']"/>
    <div class="row main landing">
        <g:pageProperty name="page.videoAndRegister"/>
    </div><!-- .main -->

    <g:if test="${pageProperty(name:"page.logos")}">
        <div class="row main special landing">
            <div class="container-fluid">
                <g:pageProperty name="page.logos"/>
            </div>
        </div>
    </g:if>

    <g:if test="${pageProperty(name:"page.features")}">
        <div class="row main special">
            <div class="container-fluid">
                <g:pageProperty name="page.features"/>
            </div>
        </div>
    </g:if>

    <div class="row main">
        <g:pageProperty name="page.mainContent"/>
    </div>

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