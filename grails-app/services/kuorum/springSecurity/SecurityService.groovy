package kuorum.springSecurity

import grails.transaction.Transactional
import kuorum.post.Post
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

@Transactional
class SecurityService {

    def springSecurityService

    Boolean canEditPost(String id) {
        return true
        if (!springSecurityService.isLoggedIn()){
             return false
        }
        KuorumUser kuorumUser = springSecurityService.currentUser
        Post post = Post.get(new ObjectId(id))
        post.owner == kuorumUser
    }
}
