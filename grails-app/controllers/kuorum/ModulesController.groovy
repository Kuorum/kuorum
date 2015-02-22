package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.project.ProjectBasicStats
import kuorum.core.model.search.Pagination
import kuorum.project.Project
import kuorum.post.Post
import kuorum.users.KuorumUser

class ModulesController {

    def springSecurityService
    def postService
    def projectService
    def projectStatsService
    def notificationService
    def kuorumUserService

    private static final Long NUM_RELEVANT_FOOTER_USERS = 23
    private static final Long NUM_RELEVANT_PROJECT = 3

    def bottomProjectStats(String hashtag){
        Project project = Project.findByHashtag(hashtag.encodeAsHashtag())
        ProjectBasicStats projectStats = projectStatsService.calculateProjectStats(project)
        [projectStats:projectStats]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userProfile() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Integer numPosts = Post.countByOwner(user)
        render template:'/modules/userProfile', model:[user:user, numPosts:numPosts]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userProfileAlerts() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        render template:'/modules/userProfileAlerts', model: [alerts:notificationService.findActiveUserAlerts(user)]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userFavorites() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        render template:'/modules/userFavorites', model:[favorites:postService.favoritesPosts(user)]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def recommendedUsers() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<KuorumUser> recommendedUsers = kuorumUserService.recommendedUsers(user, new Pagination(max:14))
        render template:'/modules/recommendedUsers', model:[recommendedUsers:recommendedUsers]
    }

    def registerFooterRelevantUsers(){
        List<KuorumUser> users = kuorumUserService.recommendedUsers(new Pagination(max: NUM_RELEVANT_FOOTER_USERS))
        render template: "/layouts/footer/footerRegisterRelevantUsers", model: [users:users]
    }

    def recommendedProjects(){
        List<Project> projects = projectService.relevantProjects(new Pagination(max:NUM_RELEVANT_PROJECT))
        render template: "/dashboard/landingPageModules/relevantProjects", model: [projects:projects]
    }

}
