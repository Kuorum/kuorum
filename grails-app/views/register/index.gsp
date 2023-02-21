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
    <div class="box-ppal auto-width-center">
        <g:if test="${_domainLoginSettings.providerBasicEmailForm}">
            <g:set var="isDev" value="${grails.util.Environment.current == grails.util.Environment.DEVELOPMENT}"/>
            <g:if test="${!isDev}">
                <r:require modules="recaptcha_register"/>
            </g:if>
                       <formUtil:validateForm bean="${command}" form="sign" autocomplete="off"/>
            <form action="#" name="sign" role="form" method="POST" autocomplete="off" class="login clearfix">
                <fieldset aria-live="polite" class="modal-login-action-buttons">
                    <div class="form-group">
                        <formUtil:input
                                command="${command}"
                                field="name"
                                cssClass="form-control input-lg"
                                showCharCounter="false"
                                label="${namePlaceholderOverwritten}"
                                showLabel="true"
                                required="true"/>
                    </div>

                    <div class="form-group">
                        <formUtil:input
                                command="${command}"
                                field="email"
                                type="email"
                                id="email"
                                cssClass="form-control input-lg"
                                showLabel="true"
                                label="${emailPlaceholderOverwritten}"
                                required="true"/>
                    </div>

                    <div class="form-group">
                        <formUtil:input
                                command="${command}"
                                field="password"
                                type="password"
                                id="password"
                                showLabel="true"
                                placeHolder="****"
                                cssClass="form-control input-lg"
                                required="true"/>
                    </div>

                    <div class="form-group">
                        %{--<input type="checkbox" name="conditions" id="conditions"/>--}%
                        %{--<g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/>--}%
                        <formUtil:checkBox
                                command="${command}"
                                field="conditions"
                                label="${g.message(code: 'register.conditions', args: [g.createLink(mapping: 'footerTermsUse')], encodeAs: 'raw')}"/>

                    </div>
                </fieldset>

                <div class="form-group center">

                    <button id="register-submit"
                            data-recaptcha=""
                            data-callback="registerCallback"
                            class="btn btn-lg g-recaptcha col-xs-12">${g.message(code: 'register.email.form.submit')}</button>

                    <div class="col-xs-12 sign-in-option">
                        <g:message code="login.intro.loginAfter"
                                   args="[g.createLink(mapping: 'login'), 'change-home-register']"/>
                    </div>
                </div>
            </form>
        </g:if>
        <g:elseif test="${!_domainLoginSettingsSocialLoginSectionActive}">
            <div class="center">
                <h3><g:message code="login.intro.loginAfter.noRegister.title"/></h3>

                <p><g:message code="login.intro.loginAfter.noRegister.login"
                              args="[g.createLink(mapping: 'login'), 'change-home-login']"/></p>
            </div>
        </g:elseif>
    </div>

    <div class="login-social center">
        <g:render template="/register/registerSocial"/>
    </div>
</content>

