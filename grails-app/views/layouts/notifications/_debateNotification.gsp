<%@ page import="kuorum.notifications.DebateAlertNotification" %>

<g:set var="postType">
    <g:link mapping="postShow" params="${notification.post.encodeAsLinkProperties()}">
        <g:message code="${kuorum.core.model.PostType.name}.${notification.post.postType}"/>
    </g:link>
</g:set>
<g:if test="${notification.instanceOf(DebateAlertNotification)}">
    <g:set var="text">
        <g:message code="notifications.debateAlertNotification.text" args="[postType]" encodeAs="raw"/>
    </g:set>
    <g:set var="answerLink" value="${createLink(mapping: 'postShow', params: notification.post.encodeAsLinkProperties())}#debates"/>
</g:if>
<g:elseif test="${notification.user == notification.post.owner}">
    <g:set var="text">
        <g:message code="notifications.debateNotification.ownerText" args="[postType]" encodeAs="raw"/>
    </g:set>
    <g:set var="answerLink" value="${createLink(mapping: 'postShow', params: notification.post.encodeAsLinkProperties())}#debates"/>
</g:elseif>
<g:else>
    <g:set var="postOwner">
        <g:link mapping="userShow" params="${notification.post.owner.encodeAsLinkProperties()}">
            ${notification.post.owner.name.encodeAsHTML()}
        </g:link>
    </g:set>
    <g:set var="text">
        <g:message code="notifications.debateNotification.text" args="[postOwner,postType]" encodeAs="raw"/>
    </g:set>
    <g:set var="answerLink" value="${createLink(mapping: 'postShow', params: notification.post.encodeAsLinkProperties())}#debates"/>

</g:else>

<g:render
        template="/layouts/notifications/notification"
        model="[
                notification:notification,
                user:notification.debateWriter,
                newNotification:newNotification,
                text:text,
                answerLink:answerLink,
                modalUser:modalUser,
                modalVictory:false
        ]"/>
