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
    ${_domainName}
</content>
<content tag="mainContent">
    <div class="box-ppal auto-width-center">
        <formUtil:validateForm bean="${command}" form="sign"/>
        <g:form mapping="joinDomain" autocomplete="off" method="POST" name="sign"
                role="form">
            <fieldset aria-live="polite">
                <div class="form-group">
                    <formUtil:input
                            command="${command}"
                            field="qrCode"
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
                            class="btn btn-lg g-recaptcha col-xs-12">
                        <g:message code="springSecurity.CodeJoinCommand.submit"/>
                    </button>
                </div>
                <g:set var="isDev" value="${grails.util.Environment.current == grails.util.Environment.DEVELOPMENT}"/>
                <g:if test="${!isDev}">
                    <r:require modules="recaptcha_register"/>
                </g:if>
            </fieldset>
        </g:form>
    </div>
</content>