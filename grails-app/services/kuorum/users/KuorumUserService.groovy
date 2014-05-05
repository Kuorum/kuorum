package kuorum.users

import grails.transaction.Transactional
import kuorum.Institution
import kuorum.ParliamentaryGroup
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.UserType
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.post.Post

@Transactional
class KuorumUserService {

    def notificationService
    def indexSolrService
    def kuorumMailService

    def createFollower(KuorumUser follower, KuorumUser following) {
        if (follower == following){
            throw new KuorumException("No se pude seguir a uno mismo","error.following.sameUser")
        }
        if (follower.following.contains(following.id)){
            log.warn("Se ha intentado seguir a un usuario que ya exisitía")
        }else{
            KuorumUser.collection.update([_id:follower.id],['$addToSet':[following:following.id]])
            KuorumUser.collection.update([_id:following.id],['$addToSet':[followers:follower.id]])
            follower.refresh()
            following.refresh()
            following.numFollowers = following.followers.size()
            following.save(flush: true)
    //        follower.following.add(following.id)
    //        following.followers.add(follower.id)
    //        follower.save()
    //        following.save()
            notificationService.sendFollowerNotification(follower, following)
        }
    }

    def deleteFollower(KuorumUser follower, KuorumUser following) {
        if (follower == following){
            throw new KuorumException("No se pude seguir a uno mismo","error.following.sameUser")
        }
        if (!follower.following.contains(following.id)){
            log.warn("Se ha intentado eliminar un follower que no existia")
        }else{
            KuorumUser.collection.update([_id:follower.id],['$pull':[following:following.id]])
            KuorumUser.collection.update([_id:following.id],['$pull':[followers:follower.id]])
            follower.refresh()
            following.refresh()
            following.numFollowers = following.followers.size()
            following.save(flush: true)
        }
    }

    KuorumUser convertAsUser(KuorumUser user){
        user.userType = UserType.PERSON
        user.personalData.userType = UserType.PERSON
        user.institution = null
        user.parliamentaryGroup = null
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.remove(rolePolitician)
        user.save()
    }

    KuorumUser convertAsOrganization(KuorumUser user){
        user.userType = UserType.ORGANIZATION
        user.personalData.userType = UserType.ORGANIZATION
        user.institution = null
        user.parliamentaryGroup = null
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.remove(rolePolitician)
        user.save()
    }

    KuorumUser convertAsPolitician(KuorumUser user, Institution institution,  ParliamentaryGroup parliamentaryGroup){
        if (!institution || !parliamentaryGroup){
            throw new KuorumException("Un politico debe de tener institucion y grupo parlamentario","error.politician.politicianData")
        }
        user.userType = UserType.POLITICIAN
        user.personalData.userType = UserType.POLITICIAN
        user.institution = institution
        user.parliamentaryGroup = parliamentaryGroup
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.add(rolePolitician)
        user.save()
    }

    /**
     * Adds premium roles to @user
     * @param user
     * @return
     */
    KuorumUser convertAsPremium(KuorumUser user){
        RoleUser rolePremium = RoleUser.findByAuthority("ROLE_PREMIUM")
        addRole(user, rolePremium)
    }

    KuorumUser addRole(KuorumUser user, RoleUser role){
        user.authorities.add(role)
        user.lastUpdated = new Date()//Mongo is not detecting changes on list, and is not updating the user roles. Modifying a root field, object is detected as dirty and it saves the changes
        user.save(flush: true)
    }

    /**
     * Removes the premium roles
     *
     * @param user
     * @return
     */
    KuorumUser convertAsNormalUser(KuorumUser user){
        RoleUser rolePremium = RoleUser.findByAuthority("ROLE_PREMIUM")
        user.lastUpdated = new Date() //Mongo is not detecting changes on list, and is not updating the user roles. Modifying a root field, object is detected as dirty and it saves the changes
        user.authorities.remove(rolePremium)
        user.save(flush: true)
    }


    List<KuorumUser> recommendedUsers(KuorumUser user, Pagination pagination = new Pagination()){
        //TODO: Improve algorithm
        if (!user){
            KuorumUser.findAllByNumFollowersGreaterThan(-1,[sort:"numFollowers",order: "desc", max:pagination.max])
        }else{
            KuorumUser.findAllByNumFollowersGreaterThanAndEmailNotEqual(-1,user.email,[sort:"numFollowers",order: "desc", max:pagination.max])
        }
    }

    List<KuorumUser> recommendedUsers(Pagination pagination = new Pagination()){
        recommendedUsers(null, pagination)
    }

    KuorumUser updateUser(KuorumUser user){
        if (!user.save()){
            log.error("No se ha podido actualizar el usuario ${user.email}(${user.id})")
            throw KuorumExceptionUtil.createExceptionFromValidatable(user)
        }
        indexSolrService.index(user)
        kuorumMailService.mailingListUpdateUser(user)

        user
    }

    List<UserParticipating> listUserActivityPerLaw(KuorumUser user){
        def userActivity = Post.collection.aggregate(
                [$match : ['$or':[[owner:user.id], [defender:user.id]]]],
                [$group :[_id:'$law',quantity:[$sum:1]]]
        )
        List<UserParticipating> activity = []
        userActivity.results().each{
            UserParticipating userParticipating = new UserParticipating(law:Law.get(it._id), numTimes: it.quantity)
            activity << userParticipating
        }
        activity
    }
}
