<g:applyLayout name="main">

<head>
    <title><g:layoutTitle/></title>
    <g:layoutHead/>
</head>

<body>
    <g:render template="/layouts/headNoLogged" model="[extraHeadCss:'transp']"/>
    <div class="row main landing">
        <g:pageProperty name="page.videoAndRegister"/>
    </div><!-- .main -->

    <div class="row main special landing">
        <div class="container-fluid">
            <g:pageProperty name="page.logos"/>
        </div>
    </div><!-- .main.special.landing -->

    <div class="row main special">
        <div class="container-fluid">
            <g:pageProperty name="page.features"/>
        </div>
    </div>

    <div class="row main">
        <g:pageProperty name="page.testimonies"/>
    </div>

    <div class="row main presskit">
        <div class="container-fluid">
            <g:pageProperty name="page.pressKit"/>
        </div>
    </div>

    <g:render template="/layouts/fastRegisterSection"/>
    <g:render template="/layouts/footer/footer"/>
</div><!-- .container-fluid -->
</body>
</g:applyLayout>