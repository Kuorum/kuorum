<%@ page import="kuorum.notifications.DefendedPostAlert; kuorum.notifications.DebateAlertNotification" %>
<section class="boxes alerts">
    <h1>
        <span class="badgeContainer">
            <span class="fa fa-bell"></span>
            <span class="badge" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${alerts.size()?:''}</span>
        </span>
        <span class="bigger"><g:message code="notifications.alerts.title"/></span>
    </h1>
    <ul class="notifications-list">
        <g:if test="${alerts}">
            <g:each in="${alerts}" var="alert" status="i">
                <g:if test="${alert.instanceOf(DebateAlertNotification)}">
                    <g:set var="postType">
                        <g:message code="${kuorum.core.model.PostType.name}.${alert.post.postType}"/>
                    </g:set>
                    <g:set var="message">
                        <g:message code="notifications.debateAlertNotification.text" args="[postType:postType]"/>
                    </g:set>
                    <g:set var="user" value="${alert.debateWriter}"/>
                </g:if>
                <g:elseif test="${alert.instanceOf(DefendedPostAlert)}">
                    <g:set var="postType">
                        <g:message code="${kuorum.core.model.PostType.name}.${alert.post.postType}"/>
                    </g:set>
                    <g:set var="message">
                        <g:message code="notifications.defendedPostAlert.text" args="[postType:postType]"/>
                    </g:set>
                    <g:set var="user" value="${alert.defender}"/>
                </g:elseif>
                <g:render template="/modules/userProfileAlerts/userProfileAlert" model="[user:user, message:message]"/>
            </g:each>
        </g:if>
        <g:else>
            <li>NO ALERTS</li>
        </g:else>
        <li class="text-center">
            <small>
                <g:link mapping="profileNotifications"><g:message code="notifications.showAll"/></g:link></small>
        </li>
    </ul>
</section>