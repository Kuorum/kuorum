<ul class="nav navbar-nav navbar-right">

    <li class="underline" itemscope itemtype="http://schema.org/Person">
        <g:link mapping="home" class="navbar-link user-area ${nav.activeMenuCss(mappingName: "home")}">
            <span itemprop="name">${user.name}</span>
            <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img" itemprop="image">
        </g:link>
    </li>

    <li class="underline">
        <g:link mapping="discoverProjects" class="navbar-link user-area ${nav.activeMenuCss(mappingName: "discoverProjects")}">
            <span class="fa fa-briefcase fa-lg"></span>
            <span class="visible-xs"><g:message code="search.filters.SolrType.PROJECT"/></span>
        </g:link>
    </li>

    %{--<g:render template="/layouts/userHeadMessages"/>--}%
    <g:render template="/layouts/userHeadNotifications" model="[user:user, notifications:notifications]"/>


    <li class="dropdown underline" itemscope itemtype="http://schema.org/Person">
        <a data-target="#" href="#" id="open-user-options" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
            <span class="fa fa-gear fa-lg"></span>
            <span class="visible-xs"><g:message code="head.logged.option"/></span>
        </a>
        <g:render template="/layouts/headUserMenuDropDown" model="[user:user, numFavorites:user.favorites.size(), numMessages:7]"/>
    </li>

    %{--<g:if test="${kuorum.core.model.UserType.POLITICIAN.equals(user.userType)}">--}%
        %{--<li>--}%
            %{--<g:link mapping="projectCreate" class="navbar-link new-project">--}%
                %{--<span class="symbol">&#35;</span>--}%
                %{--<span class="visible-xs"><g:message code="head.navigation.userMenu.project.new"/></span>--}%
            %{--</g:link>--}%
        %{--</li>--}%
    %{--</g:if>--}%
</ul>


%{--<sec:username/>--}%
%{--<sec:ifAnyGranted roles="ROLE_ADMIN">--}%
    %{--<g:link mapping="projectCreate" > Crear ley</g:link>--}%
%{--</sec:ifAnyGranted>--}%
