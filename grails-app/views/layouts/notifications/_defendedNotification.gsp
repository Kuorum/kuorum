<%@ page import="kuorum.notifications.DefendedPostAlert; kuorum.notifications.DefendedPostNotification" %>

<g:set var="postType">
    <g:link mapping="postShow" params="${notification.post.encodeAsLinkProperties()}"><g:message code="${kuorum.core.model.PostType.name}.${notification.post.postType}"/></g:link>
</g:set>
<g:set var="modalVictory" value="${false}"/>
%{--<g:set var="answerLink" value="${createLink(mapping: 'postShow', params: notification.post.encodeAsLinkProperties())}"/>--}%
<g:set var="text">
    <g:if test="${notification.instanceOf(DefendedPostAlert)}">
        <g:message code="notifications.defendedAlert.${notification.post.commitmentType}.text" args="[notification.defender.name,notification.kuorumUser.name, postType]" encodeAs="raw"/>
        <g:set var="modalVictory" value="${notification.isActive}"/>
        <g:set var="answerLink" value="${createLink(mapping: 'postAddVictory', params: notification.post.encodeAsLinkProperties())}"/>
    </g:if>
    <g:else>
        <g:message code="notifications.defendedNotification.${notification.post.commitmentType}.text" args="[notification.politician.name,notification.kuorumUser.name, postType]" encodeAs="raw"/>
    </g:else>
</g:set>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                user:notification.defender,
                text:text,
                answerLink:answerLink,
                newNotification:newNotification,
                modalVictory:modalVictory
        ]"/>

