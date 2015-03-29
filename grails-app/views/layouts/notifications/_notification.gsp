<g:if test="${toolsList}">

    <li>
        <span class="user" itemscope itemtype="http://schema.org/Person">
            <g:render template="/layouts/notifications/notificationUser" model="[user:user, modalUser:modalUser]"/>
            <!-- FIN POPOVER INFO USUARIO-->
            <span class="text-notification">${text}</span>
            %{--<span class="text-notification">ha apadrinado tu propuesta <a href="#">referendum para llegar al consenso</a></span>--}%

            <ul class="actions">
                <li>
                    <span class="time">
                        <small><kuorumDate:humanDate date="${notification.dateCreated}"/> </small>
                    </span>
                </li>

                <g:if test="${answerLink && notification.isActive}">
                    <li>
                        <g:render template="/layouts/notifications/notificationActionLink" model="[answerLink:answerLink, notification:notification, modalVictory:modalVictory]"/>
                        %{--<a href="#" class="btn btn-xs">Dar victoria</a>--}%
                    </li>
                </g:if>
            </ul>
        </span>
    </li>
</g:if>
<g:else>
    <li class='${newNotification?'new':''} ${user?'user':''} ${answerLink?'answerLink':''}'>
        <g:render template="/layouts/notifications/notificationUser" model="[user:user, modalUser:modalUser]"/>
        <span class="time">
            <small>
                <kuorumDate:humanDate date="${notification.dateCreated}"/>
            </small>
        </span>
        <span class="text-notification">
            ${text}
        </span>
        <g:if test="${answerLink && notification.isActive}">
            <span class="actions clearfix">
                <span class="pull-right">
                    <g:render template="/layouts/notifications/notificationActionLink" model="[answerLink:answerLink, notification:notification, modalVictory:modalVictory]"/>
                </span>
            </span>
        </g:if>
    </li>
</g:else>