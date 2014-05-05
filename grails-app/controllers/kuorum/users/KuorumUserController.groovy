package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.post.Cluck
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    static scaffold = true
    def springSecurityService
    def kuorumUserService
    def cluckService

    def show(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        [user:user]
    }

    def showCitizen(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        List<Cluck> clucks = cluckService.userClucks(user)
        List<UserParticipating> activeLaws = kuorumUserService.listUserActivityPerLaw(user)
        render (view:"show", model:[user:user, clucks:clucks, activeLaws:activeLaws])
    }

    def showOrganization(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        render (view:"show", model:[user:user])
    }

    def showPolitician(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        List<Cluck> clucks = cluckService.userClucks(user)
        List<UserParticipating> activeLaws = kuorumUserService.listUserActivityPerLaw(user)
        render (view:"show", model:[user:user])
    }

    def userFollowers(String id){

    }

    def userFollowing(String id){

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def follow(String id){
        KuorumUser following = KuorumUser.get(new ObjectId(id))
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser follower = KuorumUser.get(springSecurityService.principal.id)
        kuorumUserService.createFollower(follower, following)
        render follower.following.size()

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def unFollow(String id){
        KuorumUser following = KuorumUser.get(new ObjectId(id))
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser follower = KuorumUser.get(springSecurityService.principal.id)
        kuorumUserService.createFollower(follower, following)
        render follower.following.size()
    }

}
