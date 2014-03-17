package kuorum.users

import grails.transaction.Transactional
import kuorum.Institution
import kuorum.ParliamentaryGroup
import kuorum.core.exception.KuorumException
import kuorum.core.model.Gender

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

    KuorumUser convertAsEnterprise(KuorumUser user){

        user
    }

    KuorumUser convertAsUser(KuorumUser user){

        user
    }

    KuorumUser convertAsPolitician(KuorumUser user, Institution institution,  ParliamentaryGroup parliamentaryGroup){

        user
    }

    KuorumUser updatePersonalData(KuorumUser user, PersonalData personalData){
        user.personalData = personalData
        user.save()
    }
}
