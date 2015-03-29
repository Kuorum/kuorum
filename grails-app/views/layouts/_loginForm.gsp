<form  action='${postUrl}' method="post" name="login-header" id="login-header" role="form" class="login">
    <div class="form-group">
        <label for="j_username" class="sr-only"><g:message code="login.email.form.email.label"/></label>
        <input type="email" name="j_username" class="form-control input-lg" id="j_username" required placeholder="Email" aria-required="true">
    </div>
    <div class="form-group">
        <label for="pass-header" class="sr-only"><g:message code="login.email.form.password.label"/></label>
        <div class="input-append input-group">
            <input type="password" placeholder="${g.message(code:"login.email.form.password.label")}" value="" class="form-control input-lg" name="j_password" id="pass-header" aria-required="true" required>
            <span class="add-on input-group-addon" tabindex="100">
                <label><input type="checkbox" id="show-pass-header" name="show-password"><g:message code="login.email.form.password.show"/></label>
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