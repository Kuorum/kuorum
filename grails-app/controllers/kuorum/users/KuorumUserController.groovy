package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    static scaffold = true
    def springSecurityService
    def kuorumUserService

    def show(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        [user:user]
    }

    def showCitizen(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        render (view:"show", model:[user:user])
    }

    def showOrganization(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        render (view:"show", model:[user:user])
    }

    def showPolitician(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
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
