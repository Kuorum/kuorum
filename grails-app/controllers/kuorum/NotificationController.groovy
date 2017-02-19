package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.notifications.Notification
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

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


}
