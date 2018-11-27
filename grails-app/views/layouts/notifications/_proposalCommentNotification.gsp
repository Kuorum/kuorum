
<g:set var="proposalLink"><g:createLink mapping="debateShow" params="${notification.encodeAsLinkProperties()}" fragment="comment_${notification.comment.id}"/></g:set>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${notification.actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.proposalComment.text" args="[notification.actor.name,actorLink, proposalLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
