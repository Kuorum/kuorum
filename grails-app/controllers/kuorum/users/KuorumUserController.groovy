package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.UserType
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.post.Cluck
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    static scaffold = true
    def springSecurityService
    def kuorumUserService
    def cluckService

//    def beforeInterceptor = [action: this.&checkUser, except: 'login']
    def beforeInterceptor = [action: this.&checkUser]

    private checkUser(){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (!user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
    }

    def show(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        log.warn("Executing show user")
        switch (user.userType){
            case UserType.ORGANIZATION: return showOrganization(id); break;
            case UserType.PERSON: return showCitizen(id); break;
            case UserType.POLITICIAN: return showPolitician(id); break;
        }
    }

    def showCitizen(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        List<Cluck> clucks = cluckService.userClucks(user)
        List<UserParticipating> activeLaws = kuorumUserService.listUserActivityPerLaw(user)
        String provinceName = user.personalData.province.name
        render (view:"show", model:[user:user, clucks:clucks, activeLaws:activeLaws, provinceName:provinceName])
    }

    def showOrganization(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        if (user.userType != UserType.ORGANIZATION){

        }
        List<Cluck> clucks = cluckService.userClucks(user)
        List<UserParticipating> activeLaws = kuorumUserService.listUserActivityPerLaw(user)
        String provinceName = user.personalData.country.name
        render (view:"show", model:[user:user, clucks:clucks, activeLaws:activeLaws, provinceName:provinceName])
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
        kuorumUserService.deleteFollower(follower, following)
        render follower.following.size()
    }

}
