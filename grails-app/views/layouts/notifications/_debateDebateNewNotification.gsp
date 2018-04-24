
<g:set var="debateLink"><g:createLink mapping="debateShow" params="${notification.debate.encodeAsLinkProperties()}"/></g:set>
<g:set var="actor" value="${kuorum.users.KuorumUser.findByAliasAndDomain(notification.actorAlias, CustomDomainResolver.domain)}"/>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${actor.encodeAsLinkProperties()}"/></g:set>
<g:if test="${notification.debate.event}">
        <g:set var="text"><g:message code="notifications.eventNew.text" args="[actor.name,actorLink, debateLink]"/></g:set>
</g:if>
<g:else>
        <g:set var="text"><g:message code="notifications.debateDebateNew.text" args="[actor.name,actorLink, debateLink]"/></g:set>
</g:else>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
