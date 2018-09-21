<%@ page import="org.kuorum.rest.model.notification.*;" %>
<g:if test="${notification instanceof NotificationFollowRSDTO}">
    <g:render template="/layouts/notifications/followerNotification"  model='[notification:notification]'/>
</g:if>
<g:elseif test="${notification instanceof NotificationProposalNewRSDTO}">
    <g:render template="/layouts/notifications/debateProposalNewNotification" model='[notification:notification]'/>
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
<g:elseif test="${notification instanceof NotificationDebateNewRSDTO}">
    <g:render template="/layouts/notifications/debateDebateNewNotification" model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof NotificationPostNewRSDTO}">
    <g:render template="/layouts/notifications/postNewNotification" model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof NotificationSurveyNewRSDTO}">
    <g:render template="/layouts/notifications/surveyNewNotification" model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof org.kuorum.rest.model.notification.NotificationParticipatoryBudgetNewRSDTO}">
    <g:render template="/layouts/notifications/participatoryBudgetNewNotification" model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof org.kuorum.rest.model.notification.NotificationDistrictProposalNewRSDTO}">
    <g:render template="/layouts/notifications/districtProposalNewNotification" model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof org.kuorum.rest.model.notification.NotificationDistrictProposalSupportRSDTO}">
    <g:render template="/layouts/notifications/districtProposalSupportNotification" model='[notification:notification]'/>
</g:elseif>
<g:elseif test="${notification instanceof org.kuorum.rest.model.notification.NotificationDistrictProposalVoteRSDTO}">
    <g:render template="/layouts/notifications/districtProposalVoteNotification" model='[notification:notification]'/>
</g:elseif>
<g:elseif env="development">
    NOT DONE ${notification.class.name}
</g:elseif>