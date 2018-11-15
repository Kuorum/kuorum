package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.SearchNotifications
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import org.kuorum.rest.model.notification.NotificationPageRSDTO

class LayoutsController {

    def springSecurityService
    def notificationService
    def postService
    CausesService causesService;

    private static final MAX_HEAD_NOTIFICATIONS = 4

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHead() {
        KuorumUserSession user = springSecurityService.principal
        SearchNotifications searchNotificationsCommand = new SearchNotifications(user:user, max: MAX_HEAD_NOTIFICATIONS)
        NotificationPageRSDTO notificationsPage = notificationService.findUserNotifications(searchNotificationsCommand)

//        if (user.userType == UserType.CANDIDATE || user.userType == UserType.POLITICIAN){
        render template:'/layouts/payment/paymentHead', model:[user:user, notificationsPage:notificationsPage]
//        }else{
//            render template:'/layouts/userHead', model:[user:user, notifications:notifications]
//        }
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def headMessagesChecked(){

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHeadNoLinks(){
        [user:springSecurityService.principal]
    }
}
