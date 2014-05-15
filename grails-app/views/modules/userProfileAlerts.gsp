<%@ page import="kuorum.notifications.DefendedPostAlert; kuorum.notifications.DebateAlertNotification" %>
<g:if test="${alerts}">
    <section class="boxes alerts">
        <h1>
            <span class="badgeContainer">
                <span class="fa fa-bell"></span>
                <span class="badge" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${alerts.size()?:''}</span>
            </span>
            <span class="bigger"><g:message code="notifications.alerts.title"/></span>
        </h1>
        <ul class="notifications-list">
            <g:each in="${alerts}" var="alert" status="i">
                <g:if test="${alert.instanceOf(DebateAlertNotification)}">
                    <g:set var="postType">
                        <g:message code="${kuorum.core.model.PostType.name}.${alert.post.postType}"/>
                    </g:set>
                    <g:set var="message">
                        <g:message code="notifications.debateAlertNotification.text" args="[postType]"/>
                    </g:set>
                    <g:set var="user" value="${alert.debateWriter}"/>
                    <g:set var="answerLink" value="${createLink(mapping: 'postShow', params: alert.post.encodeAsLinkProperties())}#debates"/>
                </g:if>
                <g:elseif test="${alert.instanceOf(DefendedPostAlert)}">
                    <g:set var="postType">
                        <g:message code="${kuorum.core.model.PostType.name}.${alert.post.postType}"/>
                    </g:set>
                    <g:set var="message">
                        <g:message code="notifications.defendedPostAlert.text" args="[postType]"/>
                    </g:set>
                    <g:set var="user" value="${alert.defender}"/>
                    <g:set var="answerLink" value="#"/>
                </g:elseif>
                <g:render template="/modules/userProfileAlerts/userProfileAlert" model="[user:user, message:message, alert:alert, answerLink:answerLink]"/>
            </g:each>
            <li class="text-center">
                <small>
                    <g:link mapping="profileNotifications"><g:message code="notifications.showAll"/></g:link>
                </small>
            </li>
        </ul>
    </section>
</g:if>