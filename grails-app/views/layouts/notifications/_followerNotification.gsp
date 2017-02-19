<g:set var="text">
    <g:message code="notifications.followerNotification.text"/>
</g:set>
<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>