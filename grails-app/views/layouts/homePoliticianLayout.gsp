<g:applyLayout name="main">

<head>
    <title><g:layoutTitle/></title>
    <g:layoutHead/>
</head>

<body>
<g:render template="/layouts/headLanding"/>
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

    <div class="row main special profile">
        <div class="container-fluid">
            <g:pageProperty name="page.fastRegister"/>
        </div>
    </div>

    <g:render template="/layouts/footer/footer"/>
</div><!-- .container-fluid -->
</body>
</g:applyLayout>