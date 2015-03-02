<ul class="nav navbar-nav navbar-right">

    <li class="dropdown underline form">
        <a data-target="#" href="#" id="open-entry-user" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
            <span class="icon-user"></span> Entrar
        </a>
        <div id="entry-user" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-entry-user" role="menu">
            <g:include controller="login" action="loginForm"/>
            <g:render template="/layouts/registerForm" model="[registerCommand:registerCommand, formId:'sign-header']"/>
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