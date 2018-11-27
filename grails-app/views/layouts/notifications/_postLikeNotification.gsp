
<g:set var="postLink"><g:createLink mapping="postShow" params="${notification.post.encodeAsLinkProperties()}"/></g:set>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${notification.actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.postLike.text" args="[notification.actor.name,actorLink, postLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
