<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="register.join.head.title"/></title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
</head>
<content tag="title">
    <g:message code="register.join.head.title"/>
</content>
<content tag="mainContent">
    <div class="box-ppal auto-width-center">
        <sec:ifNotLoggedIn>
            <formUtil:validateForm bean="${command}" form="sign"/>
            <g:form mapping="joinDomain" autocomplete="off" method="post" name="sign" class="form-inline"
                    role="form">
                <fieldset aria-live="polite">
                    <div class="form-group">
                        <formUtil:input
                                command="${command}"
                                field="qrCode"
                                labelCssClass="sr-only"
                                showLabel="true"
                                showCharCounter="false"
                                required="true"/>
                    </div>

                    <div class="form-group submit-button">
                        <button type="submit"
                                id="join-code-submit"
                                data-sitekey="${siteKey}"
                                data-size="invisible"
                                data-callback='joinRegisterCallback'
                                class="btn btn-lg g-recaptcha">
                            <g:message code="springSecurity.join.form.submit"/>
                        </button>
                    </div>
                    <r:require modules="recaptcha_register"/>
                </fieldset>
            </g:form>
        </sec:ifNotLoggedIn>
    </div>
</content>