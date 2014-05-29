<ul class="nav navbar-nav navbar-right">
    <li class="underline" itemscope itemtype="http://schema.org/Person">
        <g:link mapping="home" class="navbar-link user-area ${nav.activeMenuCss(mappingName: "home")}">
            <span itemprop="name">${user.name}</span>
            <img src="${image.userImgSrc(user:user)}"alt="${user.name}" class="user-img" itemprop="image">
        </g:link>
    </li>
    <li class="dropdown underline" itemscope itemtype="http://schema.org/Person">
        <a data-target="#" href="#" id="open-user-options" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
            <span class="fa fa-gear fa-lg"></span>
            <span class="visible-xs"><g:message code="head.logged.option"/></span>
        </a>
        <g:render template="/layouts/headUserMenuDropDown" model="[numFavorites:user.favorites.size(), numUserPosts:numUserPosts, numMessages:7]"/>
    </li>
    %{--<g:render template="/layouts/userHeadMessages"/>--}%
    <g:render template="/layouts/userHeadNotifications" model="[user:user, notifications:notifications]"/>
</ul>


%{--<sec:username/>--}%
%{--<sec:ifAnyGranted roles="ROLE_ADMIN">--}%
    %{--<g:link mapping="lawCreate" > Crear ley</g:link>--}%
%{--</sec:ifAnyGranted>--}%
