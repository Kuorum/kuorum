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
    <nav:onlyPublicDomain checkLogged="false">
        <li class="${activeMapping == 'adminDomainConfigRelevantCampagins' ? 'active' : ''}">
            <g:link mapping="adminDomainConfigRelevantCampagins"><g:message
                    code="admin.menu.domainConfig.relevantCampaigns.title"/></g:link>
        </li>
    </nav:onlyPublicDomain>
    <li class="${activeMapping == 'adminDomainConfigLegalInfo' ? 'active' : ''}">
        <g:link mapping="adminDomainConfigLegalInfo"><g:message code="admin.menu.domainConfig.editLegalInfo"/></g:link>
    </li>
    <li class="${activeMapping == 'adminRequestEmailSender' ? 'active' : ''}">
        <g:link mapping="adminRequestEmailSender"><g:message
                code="admin.menu.domainConfig.requestCustomDomain"/></g:link>
    </li>
    <li class="${activeMapping == 'adminDomainValidation' ? 'active' : ''}">
        <g:link mapping="adminDomainValidation" class="clearfix">
            <g:message code="admin.menu.domainValidation.link"/>
        </g:link>
        <span
                class="fas fa-info-circle"
                data-toggle="tooltip"
                data-placement="top"
                title="${g.message(code: 'admin.menu.domainValidation.link.info')}"></span>
    </li>
    <li class="${activeMapping == 'adminDomainDelete' ? 'active' : ''}">
        <g:link mapping="adminDomainDelete"><g:message code="admin.menu.domainConfig.deleteDomain.title"/></g:link>
    </li>
</ul>
<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
    <hr/>

    <h1>Super Admin menu</h1>
    <g:render template="/admin/superAdminMenu" model="[activeMapping: activeMapping]"/>
</sec:ifAnyGranted>