<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.userNotifications"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileNotifications', menu:menu]"/>
</content>

<content tag="mainContent">
    <div aria-label="button group" role="group" class="btn-group btn-group-justified filters">
        <g:link mapping="toolsNotifications" role="button" class="btn ${searchNotificationsCommand.alerts==null?'active':''}">
            <g:message code="notifications.menu.all"/>
        </g:link>
        <g:link mapping="toolsNotifications" role="button" class="btn ${searchNotificationsCommand.alerts==true?'active':''}">
            <g:message code="notifications.menu.alerts"/>
        </g:link>
        <g:link mapping="toolsNotifications" role="button" class="btn ${searchNotificationsCommand.alerts==false?'active':''}" href="#">
            <g:message code="notifications.menu.notifications"/>
        </g:link>
    </div>

    <div class="box-ppal">
        <ul class="notifications-list" id="list-notifications-id">
            <g:render template="usrNotificationsList" model="[notifications:notifications]"/>
         </ul>
        <nav:loadMoreLink
                numElements="${notifications.size()}"
                pagination="${searchNotificationsCommand}"
                mapping="profileNotifications"
                parentId="list-notifications-id"
                formId="list-notifications-form"
        >
            <input type="hidden" name="alerts" value="${searchNotificationsCommand.alerts}"/>
        </nav:loadMoreLink>
    </div>
</content>
