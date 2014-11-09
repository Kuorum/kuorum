<li class="profile-alert" data-notification-id="${alert.id}">
    <span class="user" itemscope itemtype="http://schema.org/Person">
        <userUtil:showUser user="${user}"/>
        <span class="text-notification">${message}</span>
        <ul class="actions">
            <li>
                <g:render template="/layouts/notifications/notificationActionLink" model="[answerLink:answerLink, notification:alert, modalVictory:modalVictory]"/>
            </li>
            <li>
                <g:remoteLink
                        url="[mapping:'ajaxPostponeAlert',params:[id:alert.id]]"
                        rel="nofollow"
                        class="postpone-alert"
                        onSuccess="modalVictory.hideNotificationActions('${alert.id}')"
                >
                    <g:message code="notifications.alerts.postpone"/>
                </g:remoteLink>
            </li>
        </ul>
    </span>
</li>