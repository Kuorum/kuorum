<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.userNotifications"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.profileNotifications.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.profileNotifications.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileNotifications', menu:menu]"/>

</content>
<content tag="mainContent">
    <h1><g:message code="profile.profileNotifications.title"/></h1>
    <ul class="noti-filters">
        <li class="${searchNotificationsCommand.alerts==null?'active':''}"><g:link mapping="profileNotifications">Todas</g:link></li>
        <li class="${searchNotificationsCommand.alerts==true?'active':''}"><g:link mapping="profileNotifications" params="[alerts:true]"> Alertas</g:link></li>
        <li class="${searchNotificationsCommand.alerts==false?'active':''}"><g:link mapping="profileNotifications" params="[alerts:false]">Notificaciones</g:link></li>
        <li class="dropdown pull-right">
            %{--<a data-target="#" href="" class="dropdown-toggle text-center" id="open-order" data-toggle="dropdown" role="button">Ordenar <span class="fa fa-caret-down fa-lg"></span></a>--}%
            %{--<ul id="ordenar" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-order" role="menu">--}%
                %{--<li><a href="#">Opción 1</a></li>--}%
                %{--<li><a href="#">Opción 2</a></li>--}%
                %{--<li><a href="#">Opción 3</a></li>--}%
            %{--</ul>--}%
        </li>
    </ul>
    <ul class="list-notification" id="list-notifications-id">
        <g:render template="usrNotificationsList" model="[notifications:notifications]"/>
     </ul>
    <div class="hidden">
        <form id="list-notifications-form">
            <input type="hidden" name="alerts" value="${searchNotificationsCommand.alerts}"/>
        </form>
    </div>
    <nav:loadMoreLink
            numElements="${notifications.size()}"
            pagination="${searchNotificationsCommand}"
            mapping="profileNotificationsSeeMore"
            parentId="list-notifications-id"
            formId="list-notifications-form"
    />
</content>
