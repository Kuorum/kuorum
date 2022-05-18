<ul id="user-options" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-options" role="menu">
    <li><g:message code="head.navigation.userMenu.title"/></li>
    <li><g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url"><g:message code="head.navigation.userMenu.myProfile"/></g:link></li>
    <li><g:link mapping="profileEditUser" itemprop="url"><g:message code="head.navigation.userMenu.editUser"/></g:link></li>
    <li><g:link mapping="profileEditAccountDetails" itemprop="url"><g:message code="head.navigation.userMenu.configAccount"/></g:link></li>

    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <li><g:link mapping="adminPrincipal"><g:message code="admin.adminPrincipal.title"/></g:link></li>
    </sec:ifAnyGranted>

    <g:if test="${_VisibleFieldForUser}">
        <li class="hidden-xs">
            <g:if test="${_isSurveyPlatform}">
                <g:link mapping="politicianCampaigns" params="[tour:true]"><g:message code="head.navigation.userMenu.tour"/></g:link>
            </g:if>
            <g:else>
                <g:link mapping="dashboard" params="[tour:true]"><g:message code="head.navigation.userMenu.tour"/></g:link>
            </g:else>
        </li>
    </g:if>
    <li><g:link mapping="logout"><g:message code="head.logged.logout"/></g:link></li>
    <sec:ifSwitched>
        <li>
            <a href='${request.contextPath}/j_spring_security_exit_user'>
                <g:message code="switchUser.menu.backAsNormalUser" args="[sec.switchedUserOriginalUsername()]"/>
            </a>
        </li>

    </sec:ifSwitched>
    <userUtil:impersonate alias="admin" wrapper="li"/>
</ul>
