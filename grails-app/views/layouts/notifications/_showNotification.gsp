<%@ page import="kuorum.notifications.DebateNotification; kuorum.notifications.FollowerNotification; kuorum.notifications.CluckNotification" %>
<g:set var="newNotification" value="${newNotification?:false}"/>
<g:if test="${notification.instanceOf(CluckNotification)}">
    <g:render template="/layouts/notifications/cluckNotification"  model='[notification:notification,newNotification:newNotification]'/>
</g:if>
<g:elseif test="${notification.instanceOf(FollowerNotification)}">
    <g:render template="/layouts/notifications/followerNotification"  model='[notification:notification,newNotification:newNotification]'/>
</g:elseif>
<g:elseif test="${notification.instanceOf(DebateNotification)}">
    <g:render template="/layouts/notifications/debateNotification"  model='[notification:notification,newNotification:newNotification]'/>
</g:elseif>
<g:else>
    NOT DONE
</g:else>