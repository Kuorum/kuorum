<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title><g:message code="customRegister.stepDomainValidation.title"/></title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>

</head>

<content tag="mainContent">

    <div class="container-fluid box-ppal box-error">
        <div class="box-ppal-title">
            <h3><g:message code="campaign.closed.token.error.title"/></h3>
        </div>

        <div class="box-ppal-section">
            <g:message code="campaign.closed.token.error.message"
                       args="[contact.name, campaign.endDate, campagin.title]"/>
        </div>
    </div>
</content>

</html>