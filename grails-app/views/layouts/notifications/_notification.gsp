
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