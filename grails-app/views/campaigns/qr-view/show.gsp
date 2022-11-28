<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title><g:message code="tools.massMailing.view.qrView"/></title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>
    <r:require modules="qrCodeView"/>
</head>

<body>
<content tag="mainContent">
    <div class="container-fluid qr-code-container">
        <div class="row">
            <div id="qr-code" class="text-center" data-qr-width="800" data-qr-height="800">
                <!--Aqui va el QR-->
            </div>
        </div>
        <div class="row">
            <div class="text-center qr-text-container">
                <a href="${"https://" + kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.domain + "/join/" + campaign.qrCode}">
                    <span class="qr-code-text text-center">${campaign.qrCode}</span>
                </a>
            </div>
        </div>
    </div>
</content>
</body>
</html>