<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="register1ColumnLayout">
    <parameter name="extraHeadCss" value="landing"/>
</head>

<content tag="title">
    <g:message code="login.head.register"/>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="sign" autocomplete="off"/>
    <g:form mapping="register" name="sign" role="form" method="POST" autocomplete="off" class="login">
        <div class="form-group">
            <formUtil:input
                    command="${command}"
                    field="name"
                    cssClass="form-control input-lg"
                    labelCssClass="sr-only"
                    showCharCounter="false"
                    required="true"/>
        </div>
        <div class="form-group">
            <formUtil:input
                    command="${command}"
                    field="email"
                    type="email"
                    id="email"
                    cssClass="form-control input-lg"
                    labelCssClass="sr-only"
                    required="true"/>
        </div>
        <div class="form-group">
            <div id="recaptcha-register-id"></div>
            <button id="register-submit"
                    class="btn btn-lg g-recaptcha">${g.message(code:'register.email.form.submit')}</button>
            <p><g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/></p>
        </div>
        <div class="form-group">
            <p>
                <g:message code="login.intro.loginAfter" args="[g.createLink(mapping: 'login'), '']"/>
            </p>
        </div>
    </g:form>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <script>
        $(document).ready(function() {
            $('input[name=name]').focus();

            $('#register-submit').on('click', function (e) {
                e.preventDefault()
                recaptchaRegisterRender()
            });
        });

        var registerRecaptcha;
        function recaptchaRegisterRender(){
            registerRecaptcha = grecaptcha.render('recaptcha-register-id', {
                'sitekey' : '${_googleCaptchaKey}',
                'size' : 'invisible',
                'callback' : registerCallback
            });

            grecaptcha.reset(registerRecaptcha);

            grecaptcha.execute(registerRecaptcha);
        }

        function registerCallback(){
            var $form = $('#sign');
            $form.submit()
        }
    </script>
    <g:render template="/register/registerSocial"/>
</content>

