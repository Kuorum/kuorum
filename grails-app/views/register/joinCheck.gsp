<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="register.joinId.head.title"/></title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
</head>
<content tag="title">
    <g:message code="register.joinId.head.title"/>
</content>
<content tag="mainContent">
    <div class="box-ppal auto-width-center">
        <sec:ifNotLoggedIn>
            <formUtil:validateForm bean="${command}" form="sign"/>
            <g:form mapping="joinDomainId" autocomplete="off" method="post" name="sign" class="form-inline dark"
                    role="form">
                <fieldset aria-live="polite">
                    <div class="form-group">
                        <formUtil:input
                                command="${command}"
                                field="externId"
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
                                data-callback='registerCallback'
                                class="btn btn-lg g-recaptcha">
                            <g:message code="springSecurity.join.form.submit"/>
                        </button>
                    </div>
                    <input type="hidden" value="${command.campaignId}">
                    <r:require modules="recaptcha_register"/>
                </fieldset>
            </g:form>
        </sec:ifNotLoggedIn>
    </div>
</content>