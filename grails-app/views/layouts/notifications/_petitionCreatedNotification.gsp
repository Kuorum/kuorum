
<g:set var="petitionLink"><g:createLink mapping="petitionShow" params="${notification.petition.encodeAsLinkProperties()}"/></g:set>
<g:set var="actorLink"><g:createLink mapping="userShow" params="${notification.actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.newPetition.text" args="[notification.actor.name,actorLink, petitionLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                text:text
        ]"/>
