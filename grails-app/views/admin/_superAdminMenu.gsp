<ul>
    <li class="${activeMapping=='adminCreateProject'?'active':''}">
        <g:link mapping="adminCreateProject"><g:message code="admin.menu.createProject.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminUnpublishedProjects'?'active':''}">
        <g:link mapping="adminUnpublishedProjects">
            <span>
                <g:message code="admin.menu.adminUnpublishedProjects.link"/>
            </span>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="unpublished-projects" role="log" class="badge pull-right">
                ${menu.unpublishedProjects}
            </span>
        </g:link>
    </li>

    <li class="${activeMapping=='adminSearcherIndex'?'active':''}">
        <g:link mapping="adminSearcherIndex"><g:message code="admin.menu.fullIndex.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminStats'?'active':''}">
        <g:link mapping="adminStats"><g:message code="admin.menu.stats.link"/></g:link>
    </li>

    <li class="${activeMapping=='adminCreateRegion'?'active':''}">
        <g:link mapping="adminCreateRegion"><g:message code="admin.menu.adminCreateRegion.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminListRegions'?'active':''}">
        <g:link mapping="adminListRegions"><g:message code="admin.menu.adminListRegions.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminCreatePoliticalParty'?'active':''}">
        <g:link mapping="adminCreatePoliticalParty"><g:message code="admin.menu.adminCreatePoliticalParty.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminCreatePoliticalParty'?'active':''}">
        <g:link mapping="adminListPoliticalParty"><g:message code="admin.menu.adminListPoliticalParties.link"/></g:link>
    </li>
</ul>