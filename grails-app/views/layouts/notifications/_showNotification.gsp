<%@ page import="org.kuorum.rest.model.notification.*;" %>
<g:if test="${notification instanceof NotificationFollowRSDTO}">
    <g:render template="/layouts/notifications/followerNotification"  model='[notification:notification]'/>
</g:if>
<g:elseif test="${notification instanceof NotificationProposalNewRSDTO}">
    <g:render template="/layouts/notifications/debateNotification"  model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof NotificationProposalLikeRSDTO}">
    <g:render template="/layouts/notifications/proposalLikeNotification"  model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof NotificationProposalCommentRSDTO}">
    <g:render template="/layouts/notifications/proposalCommentNotification"  model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof NotificationProposalCommentMentionRSDTO}">
    <g:render template="/layouts/notifications/proposalCommentMentionNotification"  model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof NotificationProposalPinnedRSDTO}">
    <g:render template="/layouts/notifications/proposalPinnedNotification"  model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof NotificationProposalMentionRSDTO}">
    <g:render template="/layouts/notifications/proposalMentionNotification"  model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof NotificationPostLikeRSDTO}">
    <g:render template="/layouts/notifications/postLikeNotification"  model='[notification:notification]'/>
</g:elseif>
<g:elseif env="development">
    NOT DONE ${notification.class.name}
</g:elseif>