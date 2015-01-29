<ul>
    %{--<li class="${activeMapping=='discoverProjects'?'active':''}">--}%
        %{--<g:link mapping="discoverProjects"><g:message code="discover.menu.projects"/></g:link>--}%
    %{--</li>--}%
    <g:each in="${dynamicDiscoverProjects}" var="dynamicDiscoverProject">
        <li class="${activeMapping=='discoverProjects' && params.iso3166_2==dynamicDiscoverProject.region.iso3166_2?'active':''}">
            <g:link mapping="discoverProjectsByRegion" params="${dynamicDiscoverProject.region.encodeAsLinkProperties()}">
                <span>
                    <g:message code="page.title.discover.projectsByRegion" args="[dynamicDiscoverProject.region.name]"/>
                </span>
                <span aria-relevant="additions" aria-live="assertive" aria-labelledby="unpublished-projects" role="log" class="badge pull-right">
                    ${dynamicDiscoverProject.numProjects}
                </span>
            </g:link>

        </li>
    </g:each>
    <li class="${activeMapping=='discoverPoliticians'?'active':''}">
        <g:link mapping="discoverPoliticians"><g:message code="discover.menu.politicians"/></g:link>
    </li>
    <li class="${activeMapping=='discoverRecommendedPosts'?'active':''}">
        <g:link mapping="discoverRecommendedPosts"><g:message code="discover.menu.recommendedPosts"/></g:link>
    </li>
    <li class="${activeMapping=='discoverRecentPosts'?'active':''}">
        <g:link mapping="discoverRecentPosts"><g:message code="discover.menu.recentPosts"/></g:link>
    </li>

</ul>