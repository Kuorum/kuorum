<g:set var="text">
    <g:message code="notifications.victoryNotification.text" args="[notification.politician.name]" encodeAs="raw"/>
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

