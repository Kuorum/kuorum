<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/></title>
    <meta name="layout" content="homeLayout">
    <parameter name="preFooter" value="true"/>
</head>


<content tag="mainContent">
    <section id="main" role="main" class="home">
        <h1><g:message code="landingPage.title"/> </h1>

        <div class="images">
            <div class="container-fluid">
                <div class="col-xs-12 col-sm-7 col-md-7">
                    <div class="play">
                        <a href="#" class="front"><span class="fa fa-play-circle fa-3x"></span>
                        </a><g:message code="landingPage.youtube.play"/>
                    </div>
                    <iframe class="youtube" itemprop="video" height="360"
                            src="//www.youtube.com/embed/fQDQO4VRpF8?fs=1&rel=0&showinfo=0&showsearch=0&autoplay=0"
                            frameborder="0" allowfullscreen></iframe>
                </div>

                <div class="col-xs-12 col-sm-5 col-md-5">
                    <div class="signContainer">
                        <g:set var="loginLink" value="${createLink(mapping: 'login')}" />
                        <h2><g:message code="landingPage.register.title" args="[loginLink]" encodeAs="raw"/> </h2>
                        <formUtil:validateForm bean="${command}" form="sign"/>
                        <g:form mapping="register" name="sign" role="form" method="POST">
                            <div class="form-group">
                                <formUtil:input
                                        command="${command}"
                                        labelCssClass="sr-only"
                                        placeHolder="${g.message(code:'springSecurity.KuorumRegisterCommand.name.label')}"
                                        field="name"
                                        required="true"/>
                                %{--<label for="name" class="sr-only">Nombre</label>--}%
                                %{--<input type="text" class="form-control input-lg" id="name" placeholder="Nombre"--}%
                                       %{--required>--}%
                            </div>

                            <div class="form-group">
                                <formUtil:input
                                        command="${command}"
                                        labelCssClass="sr-only"
                                        placeHolder="${g.message(code:'springSecurity.KuorumRegisterCommand.email.label')}"
                                        field="email"
                                        required="true"/>
                                %{--<label for="email" class="sr-only">Correo electrónico</label>--}%
                                %{--<input type="email" class="form-control input-lg" id="email" placeholder="email"--}%
                                       %{--required>--}%
                            </div>

                            <div class="form-group clearfix">
                                <label class="checkbox-inline">
                                    <input type="checkbox" id="show-pass" value="mostrar">
                                    <g:message code="login.email.form.password.show"/>
                                </label>
                                <formUtil:input
                                        type="password"
                                        command="${command}"
                                        labelCssClass="sr-only"
                                        placeHolder="${g.message(code:'springSecurity.KuorumRegisterCommand.password.label')}"
                                        field="password"
                                        required="true"/>
                            </div>
                            <div class="form-group">
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="conditions" id="accept" value="true" class="${hasErrors(bean: command, field: 'conditions', 'error')}">
                                    <g:message code="register.conditions" args="[g.createLink(mapping: 'footerTermsUse'),g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/>

                                    <g:if test="${hasErrors(bean: command, field: 'conditions', 'error')}">
                                        <span for="password" class="error">${g.fieldError(bean: command, field: 'conditions')}</span>
                                    </g:if>
                                </label>
                            </div>
                            <div class="form-group btns">
                                <input type="submit" class="btn btn-lg btn-block" value="${message(code: 'landingPage.register.form.submit')}">
                            </div>
                        </g:form>

                        <h3><g:message code="login.rrss.label"/> </h3>
                        <g:render template="/register/registerSocialButtons"/>
                    </div><!-- /.signContainer -->
                </div>
            </div><!-- /.container-fluid -->
        </div><!-- /.row -->
    </section>

    <div class="container-fluid">

        <aside role="complementary" class="row homeSub">
            <h1><g:message code="landingPage.complementary.title"/> </h1>

            <div class="col-xs-12 col-sm-6 col-md-3">
                <div class="wrapper">
                    <span class="fa fa-archive fa-3x"></span>
                    <h2><g:message code="landingPage.complementary.voteLaws.title"/></h2>
                    <p><g:message code="landingPage.complementary.voteLaws.description"/></p>
                </div>
            </div>

            <div class="col-xs-12 col-sm-6 col-md-3">
                <div class="wrapper">
                    <span class="fa fa-edit fa-3x"></span>
                    %{--<span class="icon-book fa-3x"></span>--}%
                    <h2><g:message code="landingPage.complementary.editLaws.title"/></h2>
                    <p><g:message code="landingPage.complementary.editLaws.description"/></p>
                </div>
            </div>

            <div class="col-xs-12 col-sm-6 col-md-3">
                <div class="wrapper">
                    <span class="icon-question38 fa-3x"></span>
                    <h2><g:message code="landingPage.complementary.parliament.title"/></h2>
                    <p><g:message code="landingPage.complementary.parliament.description"/></p>
                </div>
            </div>

            <div class="col-xs-12 col-sm-6 col-md-3">
                <div class="wrapper">
                    <span class="icon-light62 fa-3x"></span>
                    <h2><g:message code="landingPage.complementary.people.title"/></h2>
                    <p><g:message code="landingPage.complementary.people.description"/></p>
                </div>
            </div>
        </aside>

    </div>
</content>

