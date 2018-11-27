
<g:set var="proposalLink"><g:createLink mapping="debateShow" params="${notification.proposal.encodeAsLinkProperties()}" fragment="proposal_${notification.proposal.id}"/></g:set>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${notification.actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.proposalMention.text" args="[notification.actor.name,actorLink, proposalLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
