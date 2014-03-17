package kuorum.users

import grails.transaction.Transactional
import kuorum.Institution
import kuorum.ParliamentaryGroup
import kuorum.core.exception.KuorumException
import kuorum.core.model.Gender
import kuorum.core.model.UserType

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
        user.userType = UserType.PERSON
        user.personalData.userType = UserType.PERSON
        user.institution = null
        user.parliamentaryGroup = null
        user.save()
    }

    KuorumUser convertAsOrganization(KuorumUser user){
        user.userType = UserType.ORGANIZATION
        user.personalData.userType = UserType.ORGANIZATION
        user.institution = null
        user.parliamentaryGroup = null
        user.save()
    }

    KuorumUser convertAsPolitician(KuorumUser user, Institution institution,  ParliamentaryGroup parliamentaryGroup){
        user.userType = UserType.POLITICIAN
        user.personalData.userType = UserType.POLITICIAN
        user.institution = institution
        user.parliamentaryGroup = parliamentaryGroup
        user.save()
    }


    KuorumUser updatePersonalData(KuorumUser user, PersonalData personalData){
        user.personalData = personalData
        if (Gender.ORGANIZATION.equals(personalData.gender)){
            user.userType = UserType.ORGANIZATION
            user.personalData.userType = UserType.ORGANIZATION
        }

        user.save()
    }
}
