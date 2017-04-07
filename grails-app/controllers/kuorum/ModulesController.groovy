package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.project.Project
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserStatsService
import org.kuorum.rest.model.tag.CauseRSDTO

class ModulesController {

    def springSecurityService
    def postService
    def projectService
    def notificationService
    def kuorumUserService
    CausesService causesService;
    KuorumUserStatsService kuorumUserStatsService

    private static final Long NUM_RELEVANT_FOOTER_USERS = 23
    private static final Long NUM_RELEVANT_PROJECT = 3
    private static final Long NUM_RELEVANT_POLITICIANS = 3


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userProfile() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Integer numCauses = causesService.findSupportedCauses(user).size()
        render template:'/modules/userProfile', model:[user:user, numCauses:numCauses]
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
        render template:'/modules/recommendedUsers',
                model:[
                        recommendedUsers:recommendedUsers,
                        boxTitle:g.message(code:'modules.recommendedUsers.title')
                ]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def recommendedPoliticiansUserDashboard() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<KuorumUser> recommendedUsers = kuorumUserService.recommendUsers(user, new Pagination(max:14))
        render template:'/modules/recommendedUsers',
                model:[
                        recommendedUsers:recommendedUsers,
                        boxTitle:g.message(code:'modules.recommendedPoliticians.title'),
                        id:"extendedPolitician"
                ]
    }

    @Deprecated
    def registerFooterRelevantUsers(){
        List<KuorumUser> users = kuorumUserService.recommendedUsers(new Pagination(max: NUM_RELEVANT_FOOTER_USERS))
        render template: "/layouts/footer/footerRegisterRelevantUsers", model: [users:users]
    }

    @Deprecated
    def recommendedProjects(){
        List<Project> projects = projectService.relevantProjects(new Pagination(max:NUM_RELEVANT_PROJECT))
        render template: "/dashboard/landingPageModules/relevantProjects", model: [projects:projects]
    }

    @Deprecated
    def recommendedPoliticians(){
        KuorumUser user = null;
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<KuorumUser> politicians = kuorumUserService.recommendUsers(user, new Pagination(max:NUM_RELEVANT_POLITICIANS))
        render template: "/dashboard/landingPageModules/relevantPoliticians", model: [politicians:politicians]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userCauses() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<CauseRSDTO> supportedCauses = causesService.findSupportedCauses(user)

        if (supportedCauses.size()>10){
            supportedCauses = supportedCauses[0..9]
        }
        render template: "/dashboard/dashboardModules/supportedCauses", model:[user:user, supportedCauses:supportedCauses]
    }

}
