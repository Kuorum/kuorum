
<g:set var="commentLink"><g:createLink mapping="debateShow" params="${notification.encodeAsLinkProperties()}" fragment="comment_${notification.comment.id}"/></g:set>
<g:set var="actor" value="${kuorum.users.KuorumUser.findByAliasAndDomain(notification.actorAlias, kuorum.core.customDomain.CustomDomainResolver.domain)}"/>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.proposalCommentMention.text" args="[actor.name,actorLink, commentLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
