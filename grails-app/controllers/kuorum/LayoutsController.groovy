package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.notifications.Notification
import kuorum.users.KuorumUser

class LayoutsController {

    def springSecurityService
    def notificationService
    def postService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHead() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<Notification> notifications = notificationService.findUserNotificationNotChecked(user)
        Integer numUserPosts = postService.numUserPosts(user)
        [user:user, notifications:notifications,numUserPosts:numUserPosts]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def headNotificationsChecked(){

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def headMessagesChecked(){

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHeadNoLinks(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Integer numUserPosts = postService.numUserPosts(user)
        [user:user,numUserPosts:numUserPosts]
    }
}
