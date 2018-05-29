<ul id="user-options" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-options" role="menu">
    <li><g:message code="head.navigation.userMenu.title"/></li>
    <li><g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url"><g:message code="head.navigation.userMenu.myProfile"/></g:link></li>
    <li><g:link mapping="profileEditUser" itemprop="url"><g:message code="head.navigation.userMenu.editUser"/></g:link></li>
    <li><g:link mapping="profileEditAccountDetails" itemprop="url"><g:message code="head.navigation.userMenu.configAccount"/></g:link></li>

    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <li><g:link mapping="adminPrincipal"><g:message code="admin.adminPrincipal.title"/></g:link></li>
    </sec:ifAnyGranted>

    <li class="hidden-xs"><g:link mapping="dashboard" params="[tour:true]"><g:message code="head.navigation.userMenu.tour"/></g:link></li>

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