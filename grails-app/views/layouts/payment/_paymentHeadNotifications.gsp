<g:if test="${notificationsPage.total>0}">
    <li class="notifications dropdown underline" itemscope="" itemtype="http://schema.org/Person">
        <g:link mapping="ajaxHeadNotificationsChecked" class="dropdown-toggle dropdown-menu-right navbar-link user-area" data-toggle="dropdown" role="button">
            <span class="fal fa-bell fa-lg"></span>
            <span class="badge" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${notificationsPage.totalUnchecked?:''}</span>
        </g:link>
        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-notifications" role="menu">
            <ul class="notification-menu">
                <li class="hidden-xs title"><g:message code="head.navigation.userMenu.userNotifications"/> </li>
                <g:render template="/layouts/payment/paymentHeadNotificationsLi" model="[notificationsPage:notificationsPage]"/>
                <li class="see-more ${notificationsPage.total <= notificationsPage.size?'hide':''}">
                    <g:link mapping="ajaxHeadNotificationsSeeMore" elementId="see-more-notifications" data-pagination-max="${notificationsPage.size}" data-pagination-offset="${notificationsPage.page}" data-pagination-total="${notificationsPage.total}">
                        <span><g:message code="read.more"/> </span>
                    </g:link>
                </li>
            </ul>
        </div>
    </li>
</g:if>