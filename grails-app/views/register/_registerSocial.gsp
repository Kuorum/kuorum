<g:if test="${_domainLoginSettings.providerFacebook || _domainLoginSettings.providerGoogle || _domainLoginSettings.providerAoc}">
    <h2 class="social-login-buttons"><g:message code="login.rrss.label"/></h2>
    <g:render template="/register/registerSocialButtons"/>
</g:if>