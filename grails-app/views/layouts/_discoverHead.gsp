<ul class="nav navbar-nav navbar-left">
    <li class="underline">
        <g:link mapping="funnelSuccessfulStories" class="navbar-link discover ${nav.activeMenuCss(mappingName: "discoverProjects")} ${nav.activeMenuCss(mappingName: "discoverPoliticians")} ${nav.activeMenuCss(mappingName: "discoverRecentPosts")} ${nav.activeMenuCss(mappingName: "discoverRecommendedPosts")}" >
            %{--<span class="fa fa-briefcase fa-lg"></span>--}%
            <span class="text-normalize"><g:message code="head.noLogged.areYouAPolitician"/></span>
        </g:link>
    </li>
</ul>