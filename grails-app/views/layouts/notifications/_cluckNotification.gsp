<g:set var="text">
    <g:message code="notifications.cluckNotification.text" encodeAs="raw"/>
</g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                user:notification.clucker,
                text:text,
                answerLink:null,
                newNotification:newNotification,
                modalVictory:false
        ]"/>

