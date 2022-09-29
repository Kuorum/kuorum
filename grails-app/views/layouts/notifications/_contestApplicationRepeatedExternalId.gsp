<g:set var="contestApplicationLink"><g:createLink mapping="contestApplicationShow"
                                                  params="${notification.contestApplication.encodeAsLinkProperties()}"/></g:set>
<g:set var="actorLink"><g:createLink mapping="userShow"
                                     params="${notification.actor.encodeAsLinkProperties()}"/></g:set>
<g:set var="text"><g:message code="notifications.contestApplication.repeatedId.text"
                             args="[notification.actor.name, actorLink, contestApplicationLink]"/></g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification: notification,
                text        : text
        ]"/>
