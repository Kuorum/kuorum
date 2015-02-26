<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="homeLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file: 'home1.jpg')}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <parameter name="hideFooter" value="false"/>
</head>

<content tag="mainContent">
    <div class="row main">
        <div class="container-fluid">
            <div class="row">
                <section id="main" role="main" class="sign clearfix">
                    <div class="col-sm-10 col-sm-offset-1 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1">
                        <h1 class="resetTitle"><g:message code="register.confirm.success"/></h1>
                        <h2 class="resetTitle"><g:message code="register.password.stablish"/></h2>
                    </div>
                    <div class="col-sm-12 col-md-4">
                        %{--<form action="#" method="post" name="pass-go-for" id="pass-go-for" class="login pass" role="form">--}%
                        <g:form name="formResetPassword" mapping="registerPassword" class="login pass" role="form">
                            <div class="form-group">
                                <label class="sr-only" for="password1"><g:message code="register.password.label"/></label>
                                <g:passwordField required="" aria-required="true" name="password1" class="form-control input-lg" placeholder="Establece una contraseña" />
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="password2"><g:message code="register.password.repeat"/></label>
                                <g:passwordField required="" aria-required="true" name="password2" class="form-control input-lg" placeholder="Confirma la contraseña" />

                            </div>
                            <div class="form-group">
                                <g:hiddenField name="userId" value="${userId}" />
                                <g:hiddenField name="token" value="${token}" />
                                <g:submitButton name="Submit" value="Establecer contraseña" class="btn btn-lg" />
                            </div>
                        </g:form>
                    </div>

                </section>
            </div><!-- /.row-->

        </div><!-- /.conatiner-fluid -->
    </div><!-- /.row.main -->

</content>
</html>