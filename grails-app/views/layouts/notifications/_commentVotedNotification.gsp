<g:set var="postType">
    <g:message code="${kuorum.core.model.PostType.name}.${notification.post.postType}"/>
</g:set>
<g:set var="text">
    <g:message code="notifications.commentVoteNotification.text" args="[notification.text.substring(0, Math.min(notification.text.size(), 50))+'...']"/>
</g:set>
<g:set var="answerLink" value="${createLink(mapping: 'postShow', params: notification.post.encodeAsLinkProperties())}#comments"/>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                user:notification.votingUser,
                text:text,
                answerLink:answerLink,
                newNotification:newNotification,
                modalVictory:false
        ]"/>

