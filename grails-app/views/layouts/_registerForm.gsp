
<r:script>
    $(function (){
        jQuery.validator.addMethod("validate_email",function(value, element) {
            return (/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test( value ))
        }, "${g.message(code:'springSecurity.KuorumRegisterCommand.email.wrongFormat')}");

        $("#${formId}").validate({
            errorClass:'error',
            errorElement:'span',
            onkeyup: false,
            onclick: false,
            rules: {
                "name":{
                    required: true,
                    maxlength: 15
                },
                "email":{
                    required: true,
                    validate_email: true,
                    remote:{
                        url: "${g.createLink(mapping: 'registerAjaxCheckEmail')}",
                        type: "post",
                        data: {
                            email: function() {return $( "#${formId} input[name=email]" ).val();}
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
                "name":{
                    required: "${g.message(code:'springSecurity.KuorumRegisterCommand.name.nullable')}",
                    maxlength: "${g.message(code:'springSecurity.KuorumRegisterCommand.name.max.size', args:[15])}"

                },
                "email":{
                    required: "${g.message(code:'springSecurity.KuorumRegisterCommand.email.nullable')}",
                    remote:"${g.message(code:'springSecurity.KuorumRegisterCommand.email.registerCommand.username.unique')}",
                    email: "${g.message(code:'springSecurity.KuorumRegisterCommand.email.wrongFormat')}",
                }
            },
            beforeSend: function () {
                var $form = $(this)
                $form.removeClass("checked")
                console.log("REMOVE")
            },
            complete: function () {
                var $form = $(this)
                $form.addClass("checked")
                console.log("ADD")
            }
        });
    });
</r:script>
<r:require modules="recaptcha_modalRegister"/>
<g:form mapping="register" action-ajax="${g.createLink(mapping: 'registerAjax')}" autocomplete="off" method="post" name="${formId}" class="login" role="form" novalidate="novalidate">
    <div class="form-group">
        <formUtil:input
                command="${registerCommand}"
                field="name"
                cssClass="form-control input-lg"
                labelCssClass="sr-only"
                showCharCounter="false"
                required="true"/>
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
    </div>
    <div class="form-group">
        <button id="register-modal-form-id"
                data-recaptcha=""
                data-callback="registerModalCallback"
                class="btn btn-orange btn-lg g-recaptcha"><g:message code="register.email.form.submit"/>
        </button>
        %{--<input type="submit" class="btn btn-lg" value="${g.message(code:'register.email.form.submit')}">--}%
        <p><g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/></p>
    </div>
    <div class="form-group">
        <p><g:message code="login.intro.loginAfter" args="['#','change-home-login']"/></p>
    </div>
</g:form>
