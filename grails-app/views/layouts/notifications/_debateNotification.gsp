<%@ page import="kuorum.notifications.DebateAlertNotification" %>


<g:if test="${notification.instanceOf(DebateAlertNotification)}">
    <g:set var="text">
        <g:message code="notifications.debateAlertNotification.text" args="[postType]" encodeAs="raw"/>
    </g:set>
    <g:set var="answerLink" value="#"/>
</g:if>
<g:elseif test="${notification.user == notification.post.owner}">
    <g:set var="text">
        <g:message code="notifications.debateNotification.ownerText" args="[postType]" encodeAs="raw"/>
    </g:set>
    <g:set var="answerLink" value="#"/>
</g:elseif>
<g:else>
    <g:set var="postOwner">
        <g:link mapping="postShow" params="${notification.post.owner.encodeAsLinkProperties()}">
            ${notification.post.owner.name.encodeAsHTML()}"/>
        </g:link>
    </g:set>
    <g:set var="text">
        <g:message code="notifications.debateNotification.text" args="[postOwner,postType]" encodeAs="raw"/>
    </g:set>
    <g:set var="answerLink" value="#"/>

</g:else>

<g:render template="/layouts/notifications/notification" model="[user:notification.debateWriter,newNotificationi:false, text:text, answerLink:answerLink]"/>
