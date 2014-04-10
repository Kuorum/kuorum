<li class="profile-alert" data-notification-id="${alert.id}">
    <span class="user" itemscope itemtype="http://schema.org/Person">
        <user:showUser user="${user}"/>
        <span class="text-notification">${message}</span>
        <ul class="actions">
            <li><a href="#" class="btn btn-custom-primary btn-xs">Responder</a></li>
            <li>
                <g:link mapping="ajaxPostponeAlert" params="[id:alert.id]" elementId="kk" rel="nofollow" class="postpone-alert">
                    <g:message code="notifications.alerts.postpone"/>
                </g:link>
            </li>
        </ul>
    </span>
</li>