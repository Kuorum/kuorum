<ul class="nav navbar-nav navbar-right">

    <li class="underline" itemscope itemtype="http://schema.org/Person">
        <g:link mapping="home" class="navbar-link user-area ${nav.activeMenuCss(mappingName: "home")}">
            <span itemprop="name">${user.name}</span>
            <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img" itemprop="image">
        </g:link>
    </li>


    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_POLITICIAN">
        <li class="dropdown box tools">
            <a href="#" class="dropdown-toggle navbar-link special" id="open-politician-tools" tabindex="7" data-toggle="dropdown" role="button">
                <span class="sr-only">Tools</span>
                <span class="visible-xs">Tools</span>
            </a>
            <ul class="dropdown-menu politician" aria-labelledby="open-politician-tools" role="menu">
                <div class="container-fluid">
                    <li><a href="#">Social Data Analytics</a></li>
                    <li><a href="#">Contact Profiling</a></li>
                    <li><a href="#" class="active">Inbox</a></li>
                    <li><a href="#">Mass Mailing</a></li>
                    <li><a href="#">Team Management</a></li>
                </div>
            </ul>
        </li>
    </sec:ifAnyGranted>
    %{--<li class="underline">--}%
        %{--<g:link mapping="discoverProjects" class="navbar-link user-area ${nav.activeMenuCss(mappingName: "discoverProjects")}">--}%
            %{--<span class="fa fa-briefcase fa-lg"></span>--}%
            %{--<span class="visible-xs"><g:message code="search.filters.SolrType.PROJECT"/></span>--}%
        %{--</g:link>--}%
    %{--</li>--}%

    %{--<g:render template="/layouts/userHeadMessages"/>--}%
    <g:render template="/layouts/userHeadNotifications" model="[user:user, notifications:notifications]"/>


    <li class="dropdown underline" itemscope itemtype="http://schema.org/Person">
        <a data-target="#" href="#" id="open-user-options" class="navbar-link user-area ${nav.activeMenuCss(mappingName: "home")}" data-toggle="dropdown" role="button">
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
    <sec:ifAnyGranted roles="ROLE_EDITOR">
        <li class="underline">
            <g:link mapping="editorCreatePolitician" class="navbar-link user-area ${nav.activeMenuCss(mappingName: "editorCreatePolitician")}">
                <span class="icon-user-add fa-lg"></span>
                <span class="visible-xs"><g:message code="admin.createPolitician.title"/></span>
            </g:link>
        </li>
    </sec:ifAnyGranted>

</ul>


%{--<sec:username/>--}%
%{--<sec:ifAnyGranted roles="ROLE_ADMIN">--}%
    %{--<g:link mapping="projectCreate" > Crear ley</g:link>--}%
%{--</sec:ifAnyGranted>--}%
