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
    ${campaign.getTitle()}
</content>
<content tag="mainContent">
    <div class="box-ppal auto-width-center">
        <formUtil:validateForm bean="${command}" form="sign"/>
        <g:form mapping="joinDomainId" autocomplete="off" method="post" name="sign"
                role="form">
            <fieldset aria-live="polite">
                <div class="form-group">
                    <formUtil:input
                            command="${command}"
                            field="externId"
                            showLabel="true"
                            label="${labelExternalId}"
                            showCharCounter="false"
                            required="true"/>
                </div>

                <div class="form-group submit-button">
                    <button type="submit"
                            id="join-code-submit"
                            data-sitekey="${siteKey}"
                            data-size="invisible"
                            data-callback='registerCallback'
                            class="btn btn-lg g-recaptcha col-xs-12">
                        <g:message code="springSecurity.CodeJoinCommand.submit"/>
                    </button>
                </div>
                <input type="hidden" value="${command.campaignId}">
                <r:require modules="recaptcha_register"/>
            </fieldset>
        </g:form>
    </div>
</content>