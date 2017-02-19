<li class="dropdown underline" itemscope="" itemtype="http://schema.org/Person">
    <g:link mapping="ajaxHeadNotificationsChecked" class="dropdown-toggle dropdown-menu-right navbar-link user-area" data-toggle="dropdown" role="button">
        <span class="fa fa-bell fa-lg"></span>
        <span class="badge" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${notificationsPage.totalUnchecked}</span>
    </g:link>
    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-notifications" role="menu">
        <ul class="notification-menu">
            <li class="hidden-xs title">Notifications</li>
            <g:each in="${notificationsPage.data}" var="notification">
                <g:render template="/layouts/notifications/showNotification" model="[notification:notification]"/>
            </g:each>
        </ul>
        <div class="see-more">
            <a href="#" id="see-more-notifications">
                <span>see more</span>
            </a>
        </div>
    </div>
</li>