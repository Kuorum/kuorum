<ul>
    %{--<li class="${activeMapping=='discoverLaws'?'active':''}">--}%
        %{--<g:link mapping="discoverLaws"><g:message code="discover.menu.laws"/></g:link>--}%
    %{--</li>--}%
    <g:each in="${dynamicDiscoverLaws}" var="dynamicDiscoverLaw">
        <li class="${activeMapping=='discoverLaws' && params.iso3166_2==dynamicDiscoverLaw.region.iso3166_2?'active':''}">
            <g:link mapping="discoverLawsByRegion" params="${dynamicDiscoverLaw.region.encodeAsLinkProperties()}">
                <span>
                    <g:message code="page.title.discover.lawsByRegion" args="[dynamicDiscoverLaw.region.name]"/>
                </span>
                <span aria-relevant="additions" aria-live="assertive" aria-labelledby="unpublished-laws" role="log" class="badge pull-right">
                    ${dynamicDiscoverLaw.numLaws}
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