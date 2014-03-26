package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.post.Post
import kuorum.users.KuorumUser

class ModulesController {

    def springSecurityService
    def postService
    def notificationService

    def recommendedPosts() {
        KuorumUser user = null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<Post> recommendedPost = postService.recommendedPosts(user)
        [recommendedPost:recommendedPost]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userProfile() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Integer numPosts = Post.countByOwner(user)
        Integer numFollowers = user.followers.size()
        Integer numFollowing = user.following.size()
        [user:user, numPost:numPosts, numFollowers:numFollowers, numFollowing:numFollowing]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userProfileAlerts() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [alerts:notificationService.findActiveUserAlerts(user)]
    }
}
