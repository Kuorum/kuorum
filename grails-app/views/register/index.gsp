<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="hideHeadSearch" value="true"/>
</head>

<content tag="title">
    <g:message code="login.head.register"/>
</content>

<content tag="mainContent">
    <g:if test="${_domainLoginSettings.providerBasicEmailForm}">
        <r:require modules="recaptcha_register"/>
        <formUtil:validateForm bean="${command}" form="sign" autocomplete="off"/>
        <form action="#" name="sign" role="form" method="POST" autocomplete="off" class="login">
            <div class="form-group">
                <formUtil:input
                        command="${command}"
                        field="name"
                        cssClass="form-control input-lg"
                        labelCssClass="sr-only"
                        showCharCounter="false"
                        placeHolder="${namePlaceholderOverwritten}"
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
                        placeHolder="${emailPlaceholderOverwritten}"
                        required="true"/>
            </div>

            <div class="form-group">
                <formUtil:input
                        command="${command}"
                        field="password"
                        type="password"
                        id="password"
                        cssClass="form-control input-lg"
                        labelCssClass="sr-only"
                        required="true"/>
            </div>

            <div class="form-group">
                <p>
                    %{--<input type="checkbox" name="conditions" id="conditions"/>--}%
                    %{--<g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/>--}%
                    <formUtil:checkBox
                            command="${command}"
                            field="conditions"
                            label="${g.message(code: 'register.conditions', args: [g.createLink(mapping: 'footerPrivacyPolicy')], encodeAs: 'raw')}"/>

                </p>
                <button id="register-submit"
                        data-recaptcha=""
                        data-callback="registerCallback"
                        class="btn btn-lg g-recaptcha">${g.message(code: 'register.email.form.submit')}</button>

                <p>
                    <g:message code="login.intro.loginAfter"
                               args="[g.createLink(mapping: 'login'), 'change-home-register']"/>
                </p>
            </div>
        </form>
    </g:if>
    <g:elseif test="${!_domainLoginSettingsSocialLoginSectionActive}">
        <div class="center">
            <h3><g:message code="login.intro.loginAfter.noRegister.title"/></h3>

            <p><g:message code="login.intro.loginAfter.noRegister.login" args="['#', 'change-home-login']"/></p>
        </div>
    </g:elseif>
    <g:render template="/register/registerSocial"/>
</content>

