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
    def postponeAlert(String id){
        Notification notification = Notification.get(new ObjectId(id))
        if (!notification){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        notificationService.markAsInactive(user, notification, Boolean.FALSE)
        render "Ok"
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def notificationChecked(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        notificationService.markUserNotificationsAsChecked(user)
        render "Ok"
    }


}
