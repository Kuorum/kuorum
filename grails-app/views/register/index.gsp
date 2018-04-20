<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="register1ColumnLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="hideHeadSearch" value="true"/>
</head>

<content tag="title">
    <g:message code="login.head.register"/>
</content>

<content tag="mainContent">
    <r:require modules="recaptcha_register"/>
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
            <button id="register-submit"
                    data-recaptcha=""
                    data-callback="registerCallback"
                    class="btn btn-lg g-recaptcha">${g.message(code:'register.email.form.submit')}</button>
            <p><g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/></p>
        </div>
        <div class="form-group">
            <p>
                <g:message code="login.intro.loginAfter" args="[g.createLink(mapping: 'login'), '']"/>
            </p>
        </div>
    </g:form>
    <g:render template="/register/registerSocial"/>
</content>

