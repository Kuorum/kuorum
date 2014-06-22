<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/></title>
    <meta name="layout" content="homeLayout">
    <parameter name="showDefaultPreFooter" value="true"/>
</head>


<content tag="mainContent">
    <h1><g:message code="landingPage.title"/> </h1>

    <div class="images">
        <div class="container-fluid">
            <div class="col-xs-12 col-sm-7 col-md-7">
                <div class="play">
                    <a href="#" data-toggle="modal" data-target="#videoHome" class="front hidden-xs hidden-sm"><span class="fa fa-play-circle fa-3x"></span></a>
                    <a href="http://www.youtube.com/embed/fQDQO4VRpF8?fs=1&rel=0&showinfo=0&showsearch=0&autoplay=1" class="front visible-xs visible-sm" target="_blank"><span class="fa fa-play-circle fa-3x"></span></a>
                    <g:message code="landingPage.youtube.play"/>
                </div>
                <div id="videoHome" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="video" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-body">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h2 class="sr-only modal-title" id="video"><g:message code="landingPage.youtube.label"/></h2>
                                <iframe id="iframeVideo" class="youtube" itemprop="video" width="600" height="450" src="http://www.youtube.com/embed/fQDQO4VRpF8?fs=1&rel=0&showinfo=0&showsearch=0&enablejsapi=1" frameborder="0" allowfullscreen></iframe>
                            </div>
                        </div>
                    </div>
                </div><!-- /#videoHome -->
            </div>

            <div class="col-xs-12 col-sm-5 col-md-5">
                <div class="signContainer">
                    <g:set var="loginLink" value="${createLink(mapping: 'login')}" />
                    <h2><g:message code="landingPage.register.title" args="[loginLink]" encodeAs="raw"/> </h2>
                    <formUtil:validateForm bean="${command}" form="sign"/>
                    <g:form mapping="register" name="sign" role="form" method="POST" autocomplete="off">
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
                            <label class="checkbox-inline pull-right">
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
                                <g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/>

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
</content>

<content tag="subHome">
    <h1><g:message code="landingPage.complementary.title"/> </h1>

    <div class="col-xs-12 col-sm-6 col-md-3">
        <div class="wrapper">
            <span class="fa fa-edit fa-3x"></span>
            <h2><g:message code="landingPage.complementary.voteLaws.title"/></h2>
            <p><g:message code="landingPage.complementary.voteLaws.description"/></p>
        </div>
    </div>

    <div class="col-xs-12 col-sm-6 col-md-3">
        <div class="wrapper">
            <span class="icon-light62 fa-3x"></span>
            %{--<span class="icon-book fa-3x"></span>--}%
            <h2><g:message code="landingPage.complementary.editLaws.title"/></h2>
            <p><g:message code="landingPage.complementary.editLaws.description"/></p>
        </div>
    </div>

    <div class="col-xs-12 col-sm-6 col-md-3">
        <div class="wrapper">
            <span class="fa fa-archive fa-3x"></span>
            <h2><g:message code="landingPage.complementary.parliament.title"/></h2>
            <p><g:message code="landingPage.complementary.parliament.description"/></p>
        </div>
    </div>

    <div class="col-xs-12 col-sm-6 col-md-3">
        <div class="wrapper">
            <span class="icon-question38 fa-3x"></span>
            <h2><g:message code="landingPage.complementary.people.title"/></h2>
            <p><g:message code="landingPage.complementary.people.description"/></p>
        </div>
    </div>
</content>

