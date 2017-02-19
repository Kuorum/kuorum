<%@ page import="org.kuorum.rest.model.notification.*;" %>
<g:if test="${notification instanceof NotificationFollowRSDTO}">
    <g:render template="/layouts/notifications/followerNotification"  model='[notification:notification]'/>
</g:if>
<g:elseif test="${notification instanceof NotificationProposalNewRSDTO}">
    <g:render template="/layouts/notifications/debateNotification"  model='[notification:notification]'/>
</g:elseif>
<g:else>
    NOT DONE ${notification.class.name}
</g:else>