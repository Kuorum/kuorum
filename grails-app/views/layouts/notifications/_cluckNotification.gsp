<g:set var="postType">
    <g:message code="${kuorum.core.model.PostType.name}.${notification.post.postType}"/>
</g:set>
<g:set var="text">
    <g:message code="notifications.cluckNotification.text" args="[postType]" encodeAs="raw"/>
</g:set>

<g:render template="/layouts/notifications/notification" model="[user:notification.clucker, text:text, answerLink:null, newNotification:newNotification]"/>

