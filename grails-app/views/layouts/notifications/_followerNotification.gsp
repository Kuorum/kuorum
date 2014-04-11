
<g:set var="text">
    <g:message code="notifications.followerNotification.text"/>
</g:set>
<g:render template="/layouts/notifications/notification" model="[user:notification.follower,newNotification:newNotification, text:text, answerLink:null]"/>