package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.Law.LawStats
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser

class ModulesController {

    def springSecurityService
    def postService
    def lawService
    def notificationService
    def kuorumUserService

    private static final Long NUM_RELEVANT_FOOTER_USERS = 23

    def bottomLawStats(String hashtag){
        Law law = Law.findByHashtag(hashtag.encodeAsHashtag())
        LawStats lawStats = lawService.calculateLawStats(law)
        [lawStats:lawStats]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userProfile() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Integer numPosts = Post.countByOwner(user)
        [user:user, numPosts:numPosts]
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
        List<KuorumUser> users = kuorumUserService.recommendedUsers(new Pagination(max: NUM_RELEVANT_FOOTER_USERS))
        render template: "/layouts/footer/footerRegisterRelevantUsers", model: [users:users]
    }

}
