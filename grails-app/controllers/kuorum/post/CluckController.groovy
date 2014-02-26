package kuorum.post

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class CluckController {

    def springSecurityService
    def cluckService

    @Secured('isAuthenticated()')
    def createCluck(String postId) {
        KuorumUser kuorumUser = KuorumUser.get(springSecurityService.principal.id)
        Post post = Post.get(new ObjectId(postId))
        Cluck cluck = cluckService.createCluck(post, kuorumUser)
        //TODO: que se renderiza
        render "OK => ${cluck as JSON}"
    }
}
