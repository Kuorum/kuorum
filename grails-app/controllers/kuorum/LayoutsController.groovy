package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.UserType
import kuorum.core.model.search.SearchNotifications
import kuorum.notifications.Notification
import kuorum.users.KuorumUser

class LayoutsController {

    def springSecurityService
    def notificationService
    def postService
    CausesService causesService;

    private static final MAX_HEAD_NOTIFICATIONS = 4

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHead() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        SearchNotifications searchNotificationsCommand = new SearchNotifications(user:user, max: MAX_HEAD_NOTIFICATIONS)
        List<Notification> listNotifications = notificationService.findUserNotifications(searchNotificationsCommand)
        Integer numNewNotifications = listNotifications.findAll{it.dateCreated>user.lastNotificationChecked}.size()
        def notifications =[
                list: listNotifications,
                numNews: numNewNotifications
        ]
//        if (user.userType == UserType.CANDIDATE || user.userType == UserType.POLITICIAN){
            render template:'/layouts/payment/paymentHead', model:[user:user, notifications:notifications]
//        }else{
//            render template:'/layouts/userHead', model:[user:user, notifications:notifications]
//        }
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def headMessagesChecked(){

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHeadNoLinks(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }
}
