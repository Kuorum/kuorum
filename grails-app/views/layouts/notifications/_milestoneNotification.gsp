<g:set var="postType">
    <g:link mapping="postShow" params="${notification.post.encodeAsLinkProperties()}"><g:message code="${kuorum.core.model.PostType.name}.${notification.post.postType}"/></g:link>
</g:set>
<g:set var="text">
    <g:message code="notifications.milestoneNotification.text" args="[postType, notification.numVotes]" encodeAs="raw"/>
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

