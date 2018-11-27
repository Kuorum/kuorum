
<g:set var="participatoryBudgetLink"><g:createLink mapping="participatoryBudgetShow" params="${notification.participatoryBudget.encodeAsLinkProperties()}"/></g:set>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${notification.actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.participatoryBudget.text" args="[notification.actor.name,actorLink, participatoryBudgetLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
