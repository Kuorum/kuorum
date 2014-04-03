<%@ page import="kuorum.notifications.DebateNotification; kuorum.notifications.FollowerNotification; kuorum.notifications.CluckNotification" %>
<li class="dropdown underline box">
    <a data-target="#" href="#" class="dropdown-toggle dropdown-menu-right navbar-link" id="open-user-notifications" data-toggle="dropdown" role="button">
        <span class="fa fa-bell fa-lg"></span>
        <span class="visible-xs" id="alerts"><g:message code="head.logged.notifications"/></span>
        <span class="badge pull-right" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${notifications.size()}</span>
    </a>

    <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-notifications" role="menu">
        <li class="hidden-xs"><g:message code="head.logged.notifications"/> </li>
        <g:each in="${notifications}" var="notification">
            <g:if test="${notification.instanceOf(CluckNotification)}">
                <g:render template="/layouts/notifications/cluckNotification"  model='[notification:notification]'/>
            </g:if>
            <g:elseif test="${notification.instanceOf(FollowerNotification)}">
                <g:render template="/layouts/notifications/followerNotification"  model='[notification:notification]'/>
            </g:elseif>
            <g:elseif test="${notification.instanceOf(DebateNotification)}">
                <g:render template="/layouts/notifications/debateNotification"  model='[notification:notification]'/>
            </g:elseif>
            <g:else>
                <g:render template="/layouts/notifications/notification" model="[notification:notification]"/>
            </g:else>
        </g:each>
        <li>
            <span class="text-notification">aenean tempor eros ut libero hendrerit aptent taciti <a href="#">vestibulum nulla tincidunt malesuada dignissim</a></span>
            <span class="actions"><span class="pull-right"><small><a href="#">Más tarde</a></small> <button type="button" class="btn bg-ntral-dark btn-sm">Responder</button></span></span>
        </li>
        <li>
            <span itemscope itemtype="http://schema.org/Person">
                <img src="images/user.jpg" alt="nombre" class="user" itemprop="image"> <a href="#" itemprop="url"><span itemprop="name">Nombre usuario</span></a>
            </span>
            <span class="text-notification">aenean tempor eros ut libero hendrerit aptent taciti <a href="#">vestibulum nulla tincidunt malesuada dignissim</a></span>
        </li>

        <li class="text-center">
            <small>
                <g:link mapping="profileNotifications">
                    <g:message code="head.logged.notifications.showAll"/>
                </g:link>
            </small>
        </li>
    </ul>
</li>