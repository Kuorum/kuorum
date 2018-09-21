
<g:set var="districtProposalLink"><g:createLink mapping="districtProposalShow" params="${notification.districtProposal.encodeAsLinkProperties()}"/></g:set>
<g:set var="actor" value="${kuorum.users.KuorumUser.findByAliasAndDomain(notification.actorAlias, kuorum.core.customDomain.CustomDomainResolver.domain)}"/>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.districtProposal.vote.text" args="[actor.name,actorLink, districtProposalLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
