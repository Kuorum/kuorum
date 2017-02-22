
<g:set var="proposalLink"><g:createLink mapping="debateShow" params="${notification.proposal.encodeAsLinkProperties()}" fragment="proposal_${notification.proposal.id}"/></g:set>
<g:set var="text"><g:message code="notifications.proposalLike.text" args="[proposalLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
