<g:each in="${notificationsPage.data}" var="notification">
    <g:render template="/layouts/notifications/showNotification" model="[notification:notification]"/>
</g:each>
