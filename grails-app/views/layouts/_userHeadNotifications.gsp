<li class="dropdown underline box">

    <g:link mapping="ajaxHeadNotificationsChecked" class="dropdown-toggle dropdown-menu-right navbar-link" elementId="open-user-notifications" data-toggle="dropdown" role="button">
        <span class="fa fa-bell fa-lg"></span>
        <span class="visible-xs" id="alerts"><g:message code="head.logged.notifications"/></span>
        <span class="badge pull-right" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${notifications.numNews?:''}</span>
    </g:link>

    <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-notifications" role="menu">
        <li class="hidden-xs"><g:message code="head.logged.notifications"/> </li>
        <g:each in="${notifications.list}" var="notification" status="i">
            <g:set var="newNotification" value="${i<notifications.numNews}"/>
            <g:render template="/layouts/notifications/showNotification" model="[notification:notification, newNotification:newNotification]"/>
        </g:each>

        <li>
            <ul class="inline clearfix">
                <li class="text-center">
                    <g:link mapping="profileNotifications">
                        <g:message code="head.logged.notifications.showAll"/>
                    </g:link>
                </li>
            </ul>
        </li>
    </ul>
</li>