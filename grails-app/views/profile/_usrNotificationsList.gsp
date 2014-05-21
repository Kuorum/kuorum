<g:each in="${notifications}" var="notification">
    <g:render
            template="/layouts/notifications/showNotification"
            model="[notification:notification, modalUser:true, newNotification:false]"/>
</g:each>