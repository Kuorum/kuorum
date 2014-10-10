<ul>
    <li class="${activeMapping=='adminCreateLaw'?'active':''}">
        <g:link mapping="adminCreateLaw"><g:message code="admin.menu.createLaw.link"/></g:link>
    </li>
    <li class="${activeMapping=='adminUnpublishedLaws'?'active':''}">
        <g:link mapping="adminUnpublishedLaws">
            <span>
                <g:message code="admin.menu.adminUnpublishedLaws.link"/>
            </span>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="unpublished-laws" role="log" class="badge pull-right">
                ${menu.unpublishedLaws}
            </span>
        </g:link>
    </li>

    <li class="${activeMapping=='adminCreateUser'?'active':''}">
        <g:link mapping="adminCreateUser"><g:message code="admin.menu.createUser.link"/></g:link>
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
</ul>