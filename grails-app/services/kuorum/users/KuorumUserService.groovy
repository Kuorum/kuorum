package kuorum.users

import grails.transaction.Transactional
import kuorum.core.exception.KuorumException

@Transactional
class KuorumUserService {

    def notificationService

    def createFollower(KuorumUser follower, KuorumUser following) {
        if (follower == following){
            throw new KuorumException("No se pude seguir a uno mismo","error.following.sameUser")
        }
        follower.following << following
        following.followers << follower
        follower.save()
        following.save()
        notificationService.sendFollowerNotification(follower, following)

    }
}
