package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.SearchNotifications
import kuorum.register.KuorumUserSession
import org.kuorum.rest.model.notification.NotificationPageRSDTO

class LayoutsController {

    def springSecurityService
    def notificationService
    def postService
    CausesService causesService

    private static final MAX_HEAD_NOTIFICATIONS = 4

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHead() {
        KuorumUserSession user = springSecurityService.principal
        SearchNotifications searchNotificationsCommand = new SearchNotifications(user:user, max: MAX_HEAD_NOTIFICATIONS)
        NotificationPageRSDTO notificationsPage = notificationService.findUserNotifications(searchNotificationsCommand)
        render template:'/layouts/payment/paymentHead', model:[user:user, notificationsPage:notificationsPage]
    }

}
