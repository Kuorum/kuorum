
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
    <g:render template="/layouts/notifications/notificationAction" model="[answerLink:answerLink, notification:notification, modalVictory:modalVictory]"/>
</li>