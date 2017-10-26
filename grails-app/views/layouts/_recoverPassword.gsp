<r:script>
    $(function (){
        $("#pass-forget").validate({
            errorClass:'error',
            errorElement:'span',
            onkeyup: false,
            onclick: false,
            async:true,
            rules: {
                "email": {
                    required: true,
                    remote: {
                        url: "${g.createLink(mapping: 'validateResetPasswordAjax')}",
                        type: "post",
                        data: {
                            email: function () {
                                return $("#pass-forget input[name=email]").val();
                            }
                        },
                        beforeSend: function () {
                            var $form = $(this)
                            $form.removeClass("checked")
                        },
                        complete: function () {
                            var $form = $(this)
                            $form.addClass("checked")
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
</r:script>
<g:form mapping="resetPassword" name="pass-forget" role="form" class="login pass">
    <div class="form-group">
        <span class="strong"><g:message code="forgotPassword.form.title"/></span>
        <p><g:message code="forgotPassword.form.subTitle"/></p>
    </div>
    <div class="form-group">
        <label for="email" class="sr-only"><g:message code="login.email.form.email.label"/></label>
        <input type="email" name="email" class="form-control input-lg" id="email" required placeholder="Email" aria-required="true">
    </div>
    <div class="form-group">
        <input type="submit" value="${message(code: 'forgotPassword.form.submit')}" class="btn btn-lg btn-blue">
        <p>
            <a href="#" class="change-home-login">
                <g:message code="register.resetPassword.form.cancel"/>
            </a>
        </p>
    </div>
    <div class="form-group"></div>
</g:form>
<div id="pass-forget-success" style="display: none;">
    <p><strong><g:message code="forgotPasswordSuccess.intro.title"/>.</strong> <g:message code="forgotPasswordSuccess.intro.subTitle"/> </p>
</div>