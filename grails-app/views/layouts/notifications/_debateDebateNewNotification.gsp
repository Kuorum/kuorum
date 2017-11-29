
<g:set var="debateLink"><g:createLink mapping="debateShow" params="${notification.debate.encodeAsLinkProperties()}"/></g:set>
<g:set var="actor" value="${kuorum.users.KuorumUser.findByAlias(notification.actorAlias)}"/>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.debateDebateNew.text" args="[actor.name,actorLink, debateLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
