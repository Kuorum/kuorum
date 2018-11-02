<ul>
    <li class="${activeMapping=='adminDomainConfig'?'active':''}">
        <g:link mapping="adminDomainConfig"><g:message code="admin.menu.domainConfig.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminDomainConfigLanding'?'active':''}">
        <g:link mapping="adminDomainConfigLanding"><g:message code="admin.menu.domainConfig.editLanding"/></g:link>
    </li>
    <li class="${activeMapping=='adminDomainConfigLegalInfo'?'active':''}">
        <g:link mapping="adminDomainConfigLegalInfo"><g:message code="admin.menu.domainConfig.editLegalInfo"/></g:link>
    </li>
    <li class="${activeMapping=='adminDomainConfigRelevantCampagins'?'active':''}">
        <g:link mapping="adminDomainConfigRelevantCampagins"><g:message code="admin.menu.domainConfig.relevantCampaigns.title"/></g:link>
    </li>
</ul>
<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
    <hr/>
    <hr/>
    <g:render template="/admin/superAdminMenu" model="[activeMapping:activeMapping]"/>
</sec:ifAnyGranted>