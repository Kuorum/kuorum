
<g:set var="districtProposalLink"><g:createLink mapping="districtProposalShow" params="${notification.districtProposal.encodeAsLinkProperties()}"/></g:set>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${notification.actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.districtProposal.text" args="[notification.actor.name,actorLink, districtProposalLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
