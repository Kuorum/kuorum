<g:set var="text">
    <g:message code="notifications.milestoneNotification.text" args="[notification.numVotes]" encodeAs="raw"/>
</g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                toolsList:toolsList?:false,
                notification:notification,
                user:notification.clucker,
                text:text,
                answerLink:null,
                newNotification:newNotification,
                modalVictory:false
        ]"/>

