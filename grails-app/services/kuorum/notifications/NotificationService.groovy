package kuorum.notifications

import grails.transaction.Transactional
import grails.util.Environment
import kuorum.post.Cluck
import kuorum.users.KuorumUser

@Transactional
class NotificationService {

    def kuorumMailService

    def sendCluckNotification(Cluck cluck) {
        //if (cluck.owner != cluck.postOwner && Environment.current == Environment.PRODUCTION)
        if (cluck.owner != cluck.postOwner)
            kuorumMailService.sendCluckNotificationMail(cluck)
    }

    def sendFollowerNotification(KuorumUser follower, KuorumUser following){

    }
}
