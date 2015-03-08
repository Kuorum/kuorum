<form action="${postUrl}" method="post" name="login" id="login" class="login" role="form">
    <div class="form-group">
        <label for="email" class="sr-only"><g:message code="login.email.form.email.label"/></label>
        <input type="email" name="j_username" class="form-control input-lg" id="email" required placeholder="${g.message(code:'login.email.form.email.label')}" aria-required="true">
    </div>
    <div class="form-group">
        <label for="pass-home" class="sr-only"><g:message code="login.email.form.password.label"/></label>
        <div class="input-append input-group">
            <input type="password" placeholder="${g.message(code:"login.email.form.password.label")}" value="" class="form-control input-lg" name="j_password" id="pass-home" aria-required="true" required>
            <span class="add-on input-group-addon" tabindex="100">
                <label><input type="checkbox" id="show-pass-home" name="show-pass-home"><g:message code="login.email.form.password.show"/></label>
            </span>
        </div>
    </div>
    <div class="form-group">
        <input type="submit" class="btn btn-blue btn-lg" value="${g.message(code:'login.email.form.login')}"> <p class="cancel"><g:message code="login.intro.login"/> <a href="#" class="change-home-register"><g:message code="head.noLogged.register"/></a></p>
    </div>
    <div class="form-group">
        <g:link mapping="resetPassword">
            <g:message code="login.email.form.password.forgotten"/>
        </g:link>
    </div>
</form>