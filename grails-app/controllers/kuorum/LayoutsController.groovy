package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.SearchNotifications
import kuorum.notifications.Notification
import kuorum.users.KuorumUser

class LayoutsController {

    def springSecurityService
    def notificationService
    def postService

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
        Integer numUserPosts = postService.numUserPosts(user)
        [user:user, notifications:notifications,numUserPosts:numUserPosts]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def headMessagesChecked(){

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHeadNoLinks(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
//        Integer numUserPosts = postService.numUserPosts(user)
        [user:user]
    }
}
