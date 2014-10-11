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
</ul>