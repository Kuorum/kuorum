package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.notifications.Notification
import kuorum.users.KuorumUser

class LayoutsController {

    def springSecurityService
    def notificationService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHead() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<Notification> notifications = notificationService.findUserNotificationNotChecked(user)
        [user:user, notifications:notifications]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHeadNoLinks(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }
}
