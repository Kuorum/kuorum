<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.googleValidation.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminDomainConfigGoogleValidation']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.googleValidation.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.googleValidation.subtitle"/></h3>
</content>
<content tag="mainContent">
    <p id="google-search-console-section-add" class="${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.googleSiteVerification?'hidden':''}">
        <a href="" class="btn btn-lg google-search-console" id="google-search-console-section-add-button"> Añadir a Google Search Console</a>
    </p>
    <p id="google-search-console-section-success" class="${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.googleSiteVerification?'':'hidden'}">
        Este domino se ha registrado satisfactoriamente usando el código:
        <br/>
        ${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.googleSiteVerification}
    </p>

    <r:script>
        function apiCallback() {
            $("#google-search-console-section-add-button").html("<span class='fa fa-spinner fa-spin'></span>");
            window.location.reload()
        }
        function callbackError() {
            display.error("Error executing the validation process");
        }
        $(".google-search-console").on("click", function (e) {
            e.preventDefault();
            var socialButton = new SocialButton("googleValidation", apiCallback, callbackError);
            socialButton.openSocialLoginWindow()
        })
    </r:script>
</content>
