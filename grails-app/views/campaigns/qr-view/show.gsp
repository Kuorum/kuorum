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
    <g:render template="/newsletter/campaignTabs/campaignViewQr" model="[campaign: campaign]"/>
</content>
</body>
</html>