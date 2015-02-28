<ul class="nav navbar-nav navbar-right">

    <li class="dropdown underline form">
        <a data-target="#" href="#" id="open-entry-user" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
            <span class="icon-user"></span> Entrar
        </a>
        <div id="entry-user" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-entry-user" role="menu">
            <form  action='${postUrl}' method="post" name="login-header" id="login-header" role="form" class="login">
                <div class="form-group">
                    <label for="j_username" class="sr-only"><g:message code="login.email.form.email.label"/></label>
                    <input type="email" name="j_username" class="form-control input-lg" id="j_username" required placeholder="Email" aria-required="true">
                </div>
                <div class="form-group">
                    <label for="j_password" class="sr-only"><g:message code="login.email.form.password.label"/></label>
                    <div class="input-append input-group">
                        <input type="password" placeholder="${g.message(code:"login.email.form.password.label")}" value="" class="form-control input-lg" name="j_password" id="j_password" aria-required="true" required>
                        <span class="add-on input-group-addon" tabindex="100">
                            <label><input type="checkbox" id="show-password" name="show-password"><g:message code="login.email.form.password.show"/></label>
                        </span>
                    </div>

                </div>
                <div class="form-group">
                    <input type="submit" class="btn btn-blue btn-lg" value="${g.message(code:'login.email.form.login')}">
                    <p class="cancel">
                        <g:message code="head.noLogged.register_or"/>
                        <g:link mapping="register" class="change-home-register">
                            <g:message code="head.noLogged.register"/>
                        </g:link>
                    </p>
                </div>
                <div class="form-group">
                    <g:link mapping="resetPassword">
                        <g:message  code="login.email.form.password.forgotten"/>
                    </g:link>
                </div>
            </form>

            %{--<formUtil:validateForm bean="${registerCommand}" form="sign-header" autocomplete="off"/>--}%
            <g:form mapping="register" autocomplete="off" method="post" name="sign-header" class="login" role="form" novalidate="novalidate">
            %{--<g:set var="registerLink" value="${g.createLink(mapping: 'register')}"/>--}%
            %{--<form action="${registerLink}" method="post" name="sign-header" id="sign-header" class="login" role="form">--}%
                <div class="form-group">
                    <formUtil:input
                            command="${registerCommand}"
                            field="name"
                            cssClass="form-control input-lg"
                            labelCssClass="sr-only"
                            showCharCounter="false"
                            required="true"/>
                    %{--<label for="name-header" class="sr-only">Dinos tu nombre o el de tu organización</label>--}%
                    %{--<input type="text" name="name-header" class="form-control input-lg" id="name-header" required placeholder="Dinos tu nombre o el de tu organización" aria-required="true">--}%
                </div>

                <div class="form-group">
                    <formUtil:input
                            command="${registerCommand}"
                            field="email"
                            type="email"
                            id="email"
                            cssClass="form-control input-lg"
                            labelCssClass="sr-only"
                            required="true"/>
                    %{--<label for="email-header" class="sr-only">Introduce tu email</label>--}%
                    %{--<input type="email" name="email-header" class="form-control input-lg" id="email-header" required placeholder="Introduce tu email" aria-required="true">--}%
                </div>
                <div class="form-group">
                    <input type="submit" class="btn btn-lg" value="Regístrate"> <p class="cancel">o <a href="#" class="change-home-login">entra</a></p>
                </div>
                <div class="form-group">
                    Al registrarte aceptas las <a href="#" target="_blank">condiciones del servicio</a>
                </div>
            </g:form>
            %{--</form>--}%
        </div>
    </li>

    %{--<form id="login" role="form" action='${postUrl}' method='POST'>--}%
        %{--<div class="form-group">--}%
            %{--<label for="j_username"><g:message code="login.email.form.email.label"/></label>--}%
            %{--<input type="email" name="j_username" class="form-control input-lg" id="j_username" required>--}%
            %{--<g:if test="${flash.message}">--}%
                %{--<span for="titlePost" class="error">${flash.message}</span>--}%
            %{--</g:if>--}%
        %{--</div>--}%
        %{--<div class="form-group clearfix">--}%
            %{--<label for="password"><g:message code="login.email.form.password.label"/></label>--}%
            %{--<label class="checkbox-inline pull-right"><input type="checkbox" id="show-pass" value="mostrar"><g:message code="login.email.form.password.show"/> </label>--}%
            %{--<input type="password" name="j_password" class="form-control input-lg" id="password" required>--}%
        %{--</div>--}%
        %{--<div class="form-group">--}%
            %{--<g:link mapping="resetPassword" class="cancel">--}%
                %{--<g:message  code="login.email.form.password.forgotten"/>--}%
            %{--</g:link>--}%
            %{--<input type="submit" class="btn btn-grey btn-lg" value="${g.message(code:'login.email.form.login')}"/>--}%
        %{--</div>--}%
    %{--</form>--}%


    %{--<li class="underline">--}%
        %{--<g:link mapping="login" class="navbar-link">--}%
            %{--<g:message code="head.noLogged.login"/>--}%
        %{--</g:link>--}%
    %{--</li>--}%
    %{--<li>--}%
        %{--<g:link mapping="register" class="btn btn-custom-primary">--}%
            %{--<g:message code="head.noLogged.register"/>--}%
        %{--</g:link>--}%
    %{--</li>--}%
</ul>