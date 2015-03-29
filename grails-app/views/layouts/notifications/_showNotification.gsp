<%@ page import="kuorum.notifications.*" %>
<g:set var="newNotification" value="${newNotification?:false}"/>
<g:if test="${notification.instanceOf(CluckNotification)}">
    <g:render template="/layouts/notifications/cluckNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:if>
<g:elseif test="${notification.instanceOf(FollowerNotification)}">
    <g:render template="/layouts/notifications/followerNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:elseif>
<g:elseif test="${notification.instanceOf(DebateNotification)}">
    <g:render template="/layouts/notifications/debateNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:elseif>
<g:elseif test="${notification.instanceOf(MilestoneNotification)}">
    <g:render template="/layouts/notifications/milestoneNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:elseif>
<g:elseif test="${notification.instanceOf(VictoryNotification)}">
    <g:render template="/layouts/notifications/victoryNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:elseif>
<g:elseif test="${notification.instanceOf(DefendedPostNotification)}">
    <g:render template="/layouts/notifications/defendedNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:elseif>
<g:elseif test="${notification.instanceOf(CommentGenericNotification)}">
    <g:render template="/layouts/notifications/commentNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:elseif>
<g:elseif test="${notification.instanceOf(CommentMyPostNotification)}">
    <g:render template="/layouts/notifications/commentNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:elseif>
<g:elseif test="${notification.instanceOf(CommentVotedNotification)}">
    <g:render template="/layouts/notifications/commentVotedNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:elseif>
<g:elseif test="${notification.instanceOf(CommentNotification)}">
%{--TODO: Este se eliminará con el futuro porque se ha sobreescrito con los otros tipos de notificacion de comentario--}%
    <g:render template="/layouts/notifications/commentNotification"  model='[notification:notification,toolsList:toolsList?:false, newNotification:newNotification,modalUser:modalUser]'/>
</g:elseif>
<g:else>
    NOT DONE ${notification.class.name}
</g:else>