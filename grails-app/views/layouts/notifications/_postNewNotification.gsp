
<g:set var="postLink"><g:createLink mapping="postShow" params="${notification.post.encodeAsLinkProperties()}"/></g:set>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${notification.actor.encodeAsLinkProperties()}"/></g:set>
<g:if test="${notification.post.event}">
        <g:set var="text"><g:message code="notifications.eventNew.text" args="[notification.actor.name,actorLink, postLink]"/></g:set>
</g:if>
<g:else>
        <g:set var="text"><g:message code="notifications.postNew.text" args="[notification.actor.name,actorLink, postLink]"/></g:set>
</g:else>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
