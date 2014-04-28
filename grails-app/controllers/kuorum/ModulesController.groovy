package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser

class ModulesController {

    def springSecurityService
    def postService
    def notificationService
    def kuorumUserService

    def recommendedPosts() {
        KuorumUser user = null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<Post> recommendedPost = postService.recommendedPosts(user)
        String title = message(code:"modules.recommendedPosts.title")
        [recommendedPost:recommendedPost, title:title]
    }

    def recommendedLawPosts() {
        KuorumUser user = null
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<Post> recommendedPost = postService.recommendedPosts(user)
        String title = message(code:"modules.recommendedLawPosts.title")
        render view:'recommendedPosts', model:[recommendedPost:recommendedPost, title:title]
    }

    def activePeopleOnLaw(String hashtag){
        [users: KuorumUser.list()]
    }

    def bottomLawStats(Law law){
        [numPeopleWritingPosts:10]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userProfile() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Integer numPosts = Post.countByOwner(user)
        Integer numFollowers = user.followers.size()
        Integer numFollowing = user.following.size()
        [user:user, numPosts:numPosts, numFollowers:numFollowers, numFollowing:numFollowing]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userProfileAlerts() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [alerts:notificationService.findActiveUserAlerts(user)]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userFavorites() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [favorites:postService.favoritesPosts(user)]
    }

    def registerFooterRelevantUsers(){
        List<KuorumUser> users = kuorumUserService.recommendedUsers(new Pagination(max: 19))
        render template: "/layouts/footer/footerRegisterRelevantUsers", model: [users:users]
    }

}
