
<script type="text/javascript">
    $(function (){
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
</script>
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
        <input type="submit" class="btn btn-lg" value="${g.message(code:'register.email.form.submit')}">
        <p><g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/></p>
    </div>
    <div class="form-group">
        <p><g:message code="login.intro.loginAfter" args="['#','change-home-login']"/></a></p>
    </div>
</g:form>
