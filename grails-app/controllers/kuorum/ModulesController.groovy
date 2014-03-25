package kuorum

import kuorum.post.Post
import kuorum.users.KuorumUser

class ModulesController {

    def springSecurityService
    def postService

    def recommendedPosts() {
        KuorumUser user = null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<Post> recommendedPost = postService.recommendedPosts(user)
        [recommendedPost:recommendedPost]
    }
}
