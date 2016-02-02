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
                0
            </span>
        </g:link>
    </li>
</ul>