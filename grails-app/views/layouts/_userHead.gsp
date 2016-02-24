<ul class="nav navbar-nav navbar-right">

    <li class="underline" itemscope itemtype="http://schema.org/Person">
        <g:link mapping="home" class="navbar-link user-area ${nav.activeMenuCss(mappingName: "home")}">
            <span itemprop="name">${user.name}</span>
            <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img" itemprop="image">
        </g:link>
    </li>


    <sec:ifAnyGranted roles="ROLE_POLITICIAN">
        <g:set var="openNavCss"
               value="${nav.activeMenuCss(
                       mappingNames:['politicianAnalytics', 'politicianContactProfiling','politicianInbox','politicianMassMailing','politicianTeamManagement'],
                       activeCss: 'open')}"/>
        <li class="dropdown box tools ${openNavCss}">
            <g:link mapping="politicianInbox" class="navbar-link special" id="open-politician-tools" tabindex="7" role="button">
                <span class="sr-only"><g:message code="head.logged.account.tools"/></span>
                <span class="visible-xs"><g:message code="head.logged.account.tools"/></span>
            </g:link>
            <ul class="dropdown-menu politician" aria-labelledby="open-politician-tools" role="menu">
                <div class="container-fluid">
                    <li><g:link mapping="politicianAnalytics"           class="${nav.activeMenuCss(mappingName: "politicianAnalytics")}"><g:message code="head.logged.account.tools.analytics"/></g:link></li>
                    <li><g:link mapping="politicianContactProfiling"    class="${nav.activeMenuCss(mappingName: "politicianContactProfiling")}"><g:message code="head.logged.account.tools.contactProfiling"/></g:link></li>
                    <li><g:link mapping="politicianInbox"               class="${nav.activeMenuCss(mappingName: "politicianInbox")}"><g:message code="head.logged.account.tools.inbox"/></g:link></li>
                    <li><g:link mapping="politicianMassMailing"         class="${nav.activeMenuCss(mappingName: "politicianMassMailing")}"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
                    <li><g:link mapping="politicianTeamManagement"      class="${nav.activeMenuCss(mappingName: "politicianTeamManagement")}"><g:message code="head.logged.account.tools.teamManagement"/></g:link></li>
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
