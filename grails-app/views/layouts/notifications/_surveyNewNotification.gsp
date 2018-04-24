
<g:set var="surveyLink"><g:createLink mapping="surveyShow" params="${notification.survey.encodeAsLinkProperties()}"/></g:set>
<g:set var="actor" value="${kuorum.users.KuorumUser.findByAliasAndDomain(notification.actorAlias, CustomDomainResolver.domain)}"/>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.surveyNew.text" args="[actor.name,actorLink, surveyLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
