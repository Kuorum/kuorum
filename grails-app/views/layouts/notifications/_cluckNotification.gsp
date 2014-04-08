
<g:set var="text">
    <g:message code="notifications.cluckNotification.text" args="[postType]" encodeAs="raw"/>
</g:set>

<g:render template="/layouts/notifications/notification" model="[user:notification.clucker, text:text, answerLink:null, newNotificationi:false]"/>

