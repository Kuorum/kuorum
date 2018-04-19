package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.SearchNotifications
import kuorum.users.KuorumUser
import org.kuorum.rest.model.notification.NotificationPageRSDTO

class NotificationController {

    def index() {}

    def springSecurityService
    def notificationService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userAlert(){

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def notificationChecked(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        notificationService.markUserNotificationsAsChecked(user)
        render "Ok"
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def notificationSeeMore(SearchNotifications searchNotificationsCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        searchNotificationsCommand.setUser(user)
        NotificationPageRSDTO notificationsPage = notificationService.findUserNotifications(searchNotificationsCommand)
        render template: "/layouts/payment/paymentHeadNotificationsLi", model: [notificationsPage:notificationsPage]
    }


}
