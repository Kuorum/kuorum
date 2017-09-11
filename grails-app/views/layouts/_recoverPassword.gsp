<script type="text/javascript">
    $(function (){
        jQuery.validator.addMethod("validate_email",function(value, element) {
            return (/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test( value ))
        }, "${g.message(code:'springSecurity.KuorumRegisterCommand.email.wrongFormat')}");

        $("#pass-forget").validate({
            errorClass:'error',
            errorElement:'span',
            onkeyup: false,
            onclick: false,
            async:true,
            rules: {
                "email": {
                    required: true,
                    validate_email: true,
                    remote: {
                        url: "${g.createLink(mapping: 'validateResetPasswordAjax')}",
                        type: "post",
                        data: {
                            email: function () {
                                return $("#pass-forget input[name=email]").val();
                            }
                        }
                    }
                }
            },
            messages: {
                "email":{
                    required: "${g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.email.nullable')}",
                    email: "${g.message(code:'springSecurity.KuorumRegisterCommand.email.wrongFormat')}",
                    remote: "${g.message(code:'kuorum.web.commands.customRegister.ForgotUserPasswordCommand.email.register.forgotPassword.notUserNameExists')}"
                }
            }
        });
    });
</script>
<g:form mapping="resetPassword" name="pass-forget" role="form" class="login pass">
    <div class="form-group">
        <label>He olvidado mi contraseña</label>
        <p>No te preocupes, nos pasa a todos, recuérdanos tu email y te ayudamos a cambiarla.</p>
    </div>
    <div class="form-group">
        <label for="email" class="sr-only"><g:message code="login.email.form.email.label"/></label>
        <input type="email" name="email" class="form-control input-lg" id="email" required placeholder="Email" aria-required="true">
    </div>
    <div class="form-group">
        <input type="submit" value="${message(code: 'forgotPassword.form.submit')}" class="btn btn-lg">
        <p>
            %{--<g:link mapping="resetPassword">--}%
            %{--<g:message  code="login.email.form.password.forgotten"/>--}%
            %{--</g:link>--}%
            <a href="#" class="change-home-login">
                Ya me acuerdo de la contraseña
            </a>
        </p>
    </div>
</g:form>
<div id="pass-forget-success" style="display: none;">
    <h4><g:message code="forgotPasswordSuccess.intro.title"/></h4>
    <p><g:message code="forgotPasswordSuccess.intro.subTitle"/> </p>
</div>