<ul>
    <li class="${activeMapping=='adminSearcherIndex'?'active':''}">
        <g:link mapping="adminSearcherIndex"><g:message code="admin.menu.fullIndex.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminDomainConfigUploadLogo'?'active':''}">
        <g:link mapping="adminDomainConfigUploadLogo"><g:message code="admin.menu.domainConfig.uploadLogo"/></g:link>
    </li>
    <li class="${activeMapping=='adminEditDomainEmailSender'?'active':''}">
        <g:link mapping="adminEditDomainEmailSender"><g:message code="admin.menu.domainConfig.setCustomDomain"/></g:link>
    </li>
    <li class="${activeMapping == 'adminDomainConfigPlan' ? 'active' : ''}">
        <g:link mapping="adminDomainConfigPlan"><g:message
                code="admin.menu.domainConfig.adminDomainConfigPlan"/></g:link>
    </li>
    <li class="${activeMapping == 'adminDomainConfigGoogleValidation' ? 'active' : ''}">
        <g:link mapping="adminDomainConfigGoogleValidation"><g:message
                code="admin.menu.domainConfig.googleValidation.title"/></g:link>
    </li>
    <li class="${activeMapping == 'adminRecerateAllCss' ? 'active' : ''}">
        <g:link mapping="adminRecerateAllCss"><g:message
                code="admin.menu.domainConfig.adminRecerateAllCss.title"/></g:link>
    </li>
    <g:if test="${sec.username().endsWith("kuorum.org")}">
        <li class="${activeMapping == 'adminDomainCreate' ? 'active' : ''}">
            <g:link mapping="adminDomainCreate"><g:message
                    code="admin.menu.domainConfig.adminDomainCreate.title"/></g:link>
        </li>
    </g:if>
</ul>