
<g:set var="text">
        <span>made a</span>
        <g:link mapping="debateShow" params="${notification.proposal.encodeAsLinkProperties()}" fragment="proposal_${notification.proposal.id}" itemprop="url">
                <span>proposal</span>
        </g:link>
</g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
