package kuorum.users

import grails.transaction.Transactional

@Transactional
class KuorumUserService {

    def createFollower(KuorumUser follower, KuorumUser following) {
        follower.following << following
        following.followers << follower
        follower.save()
        following.save()
        //TODO NOTIFICATIOINS

    }
}
