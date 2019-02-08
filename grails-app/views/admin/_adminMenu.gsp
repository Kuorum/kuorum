<ul>
    <li class="${activeMapping=='adminDomainConfig'?'active':''}">
        <g:link mapping="adminDomainConfig"><g:message code="admin.menu.domainConfig.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminDomainConfigLanding'?'active':''}">
        <g:link mapping="adminDomainConfigLanding"><g:message code="admin.menu.domainConfig.editLanding"/></g:link>
    </li>
    <li class="${activeMapping=='adminDomainConfigUploadCarouselImages'?'active':''}">
        <g:link mapping="adminDomainConfigUploadCarouselImages"><g:message code="admin.menu.domainConfig.uploadCarouselImages"/></g:link>
    </li>
    <li class="${activeMapping=='adminDomainConfigRelevantCampagins'?'active':''}">
        <g:link mapping="adminDomainConfigRelevantCampagins"><g:message code="admin.menu.domainConfig.relevantCampaigns.title"/></g:link>
    </li>
    <li class="${activeMapping=='adminAuthorizedCampaigns'?'active':''}">
        <g:link mapping="adminAuthorizedCampaigns"><g:message code="admin.menu.domainConfig.authorizedCampaigns.title"/></g:link>
    </li>
    <li class="${activeMapping=='adminDomainConfigLegalInfo'?'active':''}">
        <g:link mapping="adminDomainConfigLegalInfo"><g:message code="admin.menu.domainConfig.editLegalInfo"/></g:link>
    </li>
    <li class="${activeMapping=='adminRequestEmailSender'?'active':''}">
        <g:link mapping="adminRequestEmailSender"><g:message code="admin.menu.domainConfig.requestCustomDomain"/></g:link>
    </li>
</ul>
<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
    <hr/>
    <h1>Super Admin menu</h1>
    <g:render template="/admin/superAdminMenu" model="[activeMapping:activeMapping]"/>
</sec:ifAnyGranted>