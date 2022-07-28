<h1><g:message code="profile.leftMenu.title"/></h1>
<ul>
    <li class="${activeMapping == 'profileEditAccountDetails' ? 'active' : ''}">
        <g:link mapping="profileEditAccountDetails"><g:message code="profile.menu.profileEditAccountDetails"/></g:link>
    </li>
    <li class="${activeMapping == 'profileChangePass' ? 'active' : ''}">
        <g:link mapping="profileChangePass"><g:message code="profile.menu.changePass"/></g:link>
    </li>
    <li class="${activeMapping == 'profilePrivateFiles' ? 'active' : ''}">
        <g:link mapping="profilePrivateFiles"><g:message code="profile.menu.adminPrivateFiles"/></g:link>
    </li>
    <g:if test="${_isSocialNetwork}">
        <li class="${activeMapping == 'profileEmailNotifications' ? 'active' : ''}">
            <g:link mapping="profileEmailNotifications"><g:message
                    code="profile.menu.profileEmailNotifications"/></g:link>
        </li>
    </g:if>
    <sec:ifAnyGranted roles="ROLE_CAMPAIGN_NEWSLETTER">
        <li class="${activeMapping == 'profileNewsletterConfig' ? 'active' : ''}">
            <g:link mapping="profileNewsletterConfig"><g:message code="profile.menu.profileNewsletterConfig"/></g:link>
        </li>
    </sec:ifAnyGranted>
    <li class="${activeMapping == 'profileDeleteAccount' ? 'active' : ''}">
        <g:link mapping="profileDeleteAccount"><g:message code="profile.menu.profileDeleteAccount"/></g:link>
    </li>
</ul>