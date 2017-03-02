
<script type="text/javascript">
    $(function (){
        $("#${formId}").validate({
            errorClass:'error',
            errorElement:'span',
            onkeyup: false,
            onclick: false,
            rules: {
                "name":{
                    required: true
                },
                "email":{
                    required: true,
                    remote:"${g.createLink(mapping: 'registerAjaxCheckEmail')}"
                }
            },
            messages: {
                "name":{
                    required: "Necesitamos un nombre para dirigirnos a ti",

                },
                "email":{
                    required: "Neceistamos un email para comunicarnos contigo",
                    remote:"Ya existe en kuorum",
                    email: "Formato de email erróneo"
                }
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
        <input type="submit" class="btn btn-lg" value="${g.message(code:'register.email.form.submit')}"> <p class="cancel"><g:message code="springSecurity.KuorumRegisterCommand.email.or"/> <a href="#" class="change-home-login"><g:message code="login.intro.loginAfter"/></a></p>
    </div>
    <div class="form-group">
        <g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/>
    </div>
</g:form>
