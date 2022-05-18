<h1><g:message code="profile.leftMenu.title"/></h1>
<ul>
    <li class="${activeMapping == 'profileEditAccountDetails' ? 'active' : ''}">
        <g:link mapping="profileEditAccountDetails"><g:message code="profile.menu.profileEditAccountDetails"/></g:link>
    </li>
    <li class="${activeMapping == 'profileChangePass' ? 'active' : ''}">
        <g:link mapping="profileChangePass"><g:message code="profile.menu.changePass"/></g:link>
    </li>
    <g:if test="${!_isSurveyPlatform}">
        <li class="${activeMapping == 'profileEmailNotifications' ? 'active' : ''}">
            <g:link mapping="profileEmailNotifications"><g:message
                    code="profile.menu.profileEmailNotifications"/></g:link>
        </li>
        <li class="${activeMapping == 'profileNewsletterConfig' ? 'active' : ''}">
            <g:link mapping="profileNewsletterConfig"><g:message code="profile.menu.profileNewsletterConfig"/></g:link>
        </li>
    </g:if>
    <li class="${activeMapping == 'profileDeleteAccount' ? 'active' : ''}">
        <g:link mapping="profileDeleteAccount"><g:message code="profile.menu.profileDeleteAccount"/></g:link>
    </li>
</ul>