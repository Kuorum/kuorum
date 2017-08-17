
<g:set var="proposalLink"><g:createLink mapping="debateShow" params="${notification.proposal.encodeAsLinkProperties()}" fragment="proposal_${notification.proposal.id}"/></g:set>
<g:set var="actor" value="${kuorum.users.KuorumUser.findByAlias(notification.actorAlias)}"/>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.proposalMention.text" args="[actor.name,actorLink, proposalLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
