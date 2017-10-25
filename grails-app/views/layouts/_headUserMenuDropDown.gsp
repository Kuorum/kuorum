<ul id="user-options" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-options" role="menu">
    <li><g:message code="head.navigation.userMenu.title"/></li>
    <li><g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url"><g:message code="head.navigation.userMenu.myProfile"/></g:link></li>
    <li><g:link mapping="profileEditUser" itemprop="url"><g:message code="head.navigation.userMenu.editUser"/></g:link></li>
    <li><g:link mapping="profileEditAccountDetails" itemprop="url"><g:message code="head.navigation.userMenu.configAccount"/></g:link></li>
    %{--<li><g:link mapping="toolsNotifications"><g:message code="head.navigation.userMenu.tools"/></g:link></li>--}%
    <li class="hidden-xs"><g:link mapping="dashboard" params="[tour:true]"><g:message code="head.navigation.userMenu.tour"/></g:link></li>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <li><g:link mapping="adminPrincipal"><g:message code="admin.adminPrincipal.title"/></g:link></li>
    </sec:ifAnyGranted>
    %{--<li>--}%
        %{--<g:link mapping="toolsFavorites">--}%
            %{--<span id="post-sin-leer"><g:message code="head.navigation.userMenu.showFavoritesPosts"/></span>--}%
            %{--<span class="badge" role="log" aria-labelledby="post-sin-leer" aria-live="assertive" aria-relevant="additions">${numFavorites?:''}</span>--}%
        %{--</g:link>--}%
    %{--</li>--}%
    %{--<li>--}%
        %{--<g:link mapping="toolsMyPosts">--}%
            %{--<span id="mis-post"><g:message code="head.navigation.userMenu.showUserPosts"/></span>--}%
            %{--<span class="badge" role="log" aria-labelledby="mis-post" aria-live="assertive" aria-relevant="additions">${numUserPosts?:''}</span>--}%
        %{--</g:link>--}%
    %{--</li>--}%
    %{--<li>--}%
        %{--<g:link mapping="profileMessages">--}%
            %{--<span id="messages-user"><g:message code="head.navigation.userMenu.userMessages"/></span>--}%
            %{--<span class="badge" role="log" aria-labelledby="messages-user" aria-live="assertive" aria-relevant="additions">${numMessages}</span>--}%
        %{--</g:link>--}%
    %{--</li>--}%
    %{--<sec:ifAnyGranted roles="ROLE_POLITICIAN">--}%
        %{--<li><g:link mapping="adminPrincipal" params="[regionName:sec.loggedInUserInfo([field:'politicianOnRegionName'])]"><sec:loggedInUserInfo field="politicianOnRegionName"/></g:link></li>--}%
    %{--</sec:ifAnyGranted>--}%
    <li><g:link mapping="logout"><g:message code="head.logged.logout"/></g:link></li>
    <sec:ifSwitched>
        <li>
            <a href='${request.contextPath}/j_spring_security_exit_user'>
                <g:message code="switchUser.menu.backAsNormalUser" args="[sec.switchedUserOriginalUsername()]"/>
            </a>
        </li>

    </sec:ifSwitched>
</ul>