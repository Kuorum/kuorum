<r:script>
    $(function (){
        $("#${modalId} #login-modal").validate({
            errorClass:'error',
            errorElement:'span',
            onkeyup: false,
            onclick: false,
            async:false,
            rules: {
                "j_username":{
                    required: true ,
                    email: true
                },
                "j_password":{
                    required: true,
                    remote:{
                        url: "${g.createLink(mapping: 'ajaxLoginCheck')}",
                        type: "post",
                        data: {
                            j_username: function() {return $( "#${modalId} input[name=j_username]" ).val();},
                            j_password: function() {return $( "#${modalId} input[name=j_password]" ).val();}
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
                "j_username":{
                    required: "${g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.email.nullable')}",
                    email: "${g.message(code:'springSecurity.KuorumRegisterCommand.email.wrongFormat')}"
                },
                "j_password":{
                    required: "${g.message(code:'springSecurity.errors.login.fail')}",
                    remote:"${g.message(code:'springSecurity.errors.login.fail')}"
                }
            }
        });
    });
</r:script>
<form action='${g.createLink(controller: 'login', action: 'modalAuth')}' method="post" name="login-header" id="login-modal" role="form" class="login">
    <div class="form-group">
        <label for="j_username" class="sr-only"><g:message code="login.email.form.email.label"/></label>
        <input type="email" name="j_username" class="form-control input-lg" id="j_username" required placeholder="Email" aria-required="true">
    </div>
    <div class="form-group">
        <label for="pass-header" class="sr-only"><g:message code="login.email.form.password.label"/></label>
        <div class="input-append input-group">
            <input type="password" placeholder="${g.message(code:"login.email.form.password.label")}" value="" class="form-control input-lg" name="j_password" id="pass-header" aria-required="true" required>
            <span class="add-on input-group-addon" tabindex="100">
                <label><input type="checkbox" class="show-hide-pass" id="show-pass-header" name="show-password"><g:message code="login.email.form.password.show"/></label>
            </span>
        </div>
    </div>
    <div class="form-group">
        <input type="submit" class="btn btn-blue btn-lg" value="${g.message(code:'login.email.form.login')}">
        <p>
            %{--<g:link mapping="resetPassword">--}%
                %{--<g:message  code="login.email.form.password.forgotten"/>--}%
            %{--</g:link>--}%
            <a href="#" class="change-home-forgot-password">
                <g:message  code="login.email.form.password.forgotten"/>
            </a>
        </p>
    </div>
    <div class="form-group">
        <p>
            <g:message code="head.noLogged.register" args="[g.createLink(mapping: 'register'),'change-home-register' ]"/>
        </p>
    </div>
</form>