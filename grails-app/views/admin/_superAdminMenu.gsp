<ul>
    <li class="${activeMapping=='adminDomainConfig'?'active':''}">
        <g:link mapping="adminDomainConfig"><g:message code="admin.menu.domainConfig.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminSearcherIndex'?'active':''}">
        <g:link mapping="adminSearcherIndex"><g:message code="admin.menu.fullIndex.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminStats'?'active':''}">
        <g:link mapping="adminStats"><g:message code="admin.menu.stats.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminEditorsMonitoring'?'active':''}">
        <g:link mapping="adminEditorsMonitoring"><g:message code="admin.menu.adminEditorsMonitoring"/></g:link>
    </li>
</ul>