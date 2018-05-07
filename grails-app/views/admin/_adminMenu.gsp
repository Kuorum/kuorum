<ul>
    <li class="${activeMapping=='adminDomainConfig'?'active':''}">
    <g:link mapping="adminDomainConfig"><g:message code="admin.menu.domainConfig.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminDomainConfigUploadLogo'?'active':''}">
    <g:link mapping="adminDomainConfigUploadLogo"><g:message code="admin.menu.domainConfig.uploadLogo"/></g:link>
    </li>
</ul>
<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
    <g:render template="/admin/superAdminMenu" model="[activeMapping:activeMapping]"/>
</sec:ifAnyGranted>