<ul id="user-options" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-options" role="menu">
    <li><g:message code="head.navigation.userMenu.title"/></li>
    <li><g:link mapping="profileEditUser" itemprop="url"><g:message code="head.navigation.userMenu.editUser"/></g:link></li>
    <li><g:link mapping="profileChangePass"><g:message code="head.navigation.userMenu.changePassword"/></g:link></li>
    <li><g:link mapping="profileEmailNotifications"><g:message code="head.navigation.userMenu.configurationEmails"/></g:link></li>
    <li>
        <g:link mapping="profileFavorites">
            <span id="post-sin-leer"><g:message code="head.navigation.userMenu.showFavoritesPosts"/></span>
            <span class="badge" role="log" aria-labelledby="post-sin-leer" aria-live="assertive" aria-relevant="additions">${numFavorites}</span>
        </g:link>
    </li>
    <li>
        <g:link mapping="profileMyPosts">
            <span id="mis-post"><g:message code="head.navigation.userMenu.showUserPosts"/></span>
            <span class="badge" role="log" aria-labelledby="mis-post" aria-live="assertive" aria-relevant="additions">${numUserPosts}</span>
        </g:link>
    </li>
    <li><g:link mapping="profileKuorumStore"><g:message code="head.navigation.userMenu.kuorumStore"/></g:link></li>
    <li><g:link mapping="profileNotifications"><g:message code="head.navigation.userMenu.userNotifications"/></g:link></li>
    <li>
        <g:link mapping="profileMessages">
            <span id="messages-user"><g:message code="head.navigation.userMenu.userMessages"/></span>
            <span class="badge" role="log" aria-labelledby="messages-user" aria-live="assertive" aria-relevant="additions">${numMessages}</span>
        </g:link>
    </li>
    <li><g:link mapping="logout"><g:message code="head.logged.logout"/></g:link></li>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <li><hr/></li>
        <li><g:link mapping="adminPrincipal"><g:message code="admin.adminPrincipal.title"/></g:link></li>
    </sec:ifAnyGranted>
</ul>