package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.post.Cluck
import kuorum.project.Project
import kuorum.project.ProjectEvent
import kuorum.solr.SearchSolrService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserStatsService
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
import payment.contact.ContactService
import springSecurity.KuorumRegisterCommand

class DashboardController {

    SpringSecurityService springSecurityService
    def cluckService
    def projectService
    def kuorumUserService
    KuorumUserStatsService kuorumUserStatsService
    CausesService causesService
    SearchSolrService searchSolrService
    ContactService contactService

    private  static final Integer MAX_PROJECT_EVENTS = 2

    def index(){
        if (springSecurityService.isLoggedIn()){
//            render(view: "dashboard", model: dashboard())
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
            render(view: "landingPoliticians", model: landingPoliticians())
        }
    }
    def dashboard() {
        if (!springSecurityService.isLoggedIn()){
            redirect(mapping:"home")
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (kuorumUserService.isPaymentUser(user)){
            Map model = buildPaymentDashboadr(user);
            if (model.contacts.total>0){
                render view: "/dashboard/payment/paymentDashboard", model: model
            }else{
                render view: "/dashboard/payment/paymentNoContactsDashboard", model: model
            }
        }else{
            buildUserDashboard(user)
        }
    }

    private Map buildUserDashboard(KuorumUser user){
        Pagination causesPagination = new Pagination(max:6)
        SuggestedCausesRSDTO causesSuggested = causesService.suggestCauses(user, causesPagination)
        Pagination politiciansDashboardPagination = new Pagination(max:6)
        List<KuorumUser> politicians = kuorumUserService.recommendPoliticians(user,politiciansDashboardPagination)
        [
                loggedUser:user,
                causesSuggested:causesSuggested,
                causesPagination:causesPagination,
                politicians:politicians,
                politiciansDashboardPagination:politiciansDashboardPagination
        ]
    }

    private def buildPaymentDashboadr(KuorumUser user){
        [contacts:contactService.getUsers(user)]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardCauses(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        SuggestedCausesRSDTO causesSuggested = causesService.suggestCauses(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${causesSuggested.total < pagination.offset}")
        render template: "/dashboard/dashboardModules/causeCardList", model:[causes:causesSuggested.data]
    }

    private def splitClucksInParts(List<Cluck> clucks){
        if (clucks && clucks.size() > 2){
            return [clucks_1:clucks[0..1], clucks_2:clucks[2..clucks.size()-1]]
        }
        return [clucks_1:clucks, clucks_2:[]]
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardPoliticians(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<KuorumUser> suggesterPoliticians=  kuorumUserService.recommendPoliticians(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${suggesterPoliticians.size() < pagination.max}")
        render template: "/dashboard/listDashboardPoliticians", model:[politicians:suggesterPoliticians]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardClucks(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ProjectEvent> projectEvents = projectService.findRelevantProjectEvents(user, new Pagination(max: MAX_PROJECT_EVENTS, offset: pagination.offset))
        List<Cluck> clucks =  cluckService.dashboardClucks(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<pagination.max}")
        render template: "/cluck/liClucks", model:[clucks:clucks, projectEvents:projectEvents]
    }

    def landingPoliticians(){
        [command: new KuorumRegisterCommand()]
    }

    def discover(){
        //TODO: QUE HACER SI NO ES ESPA�A
        Region region = Region.findByIso3166_2("EU-ES")
        redirect mapping:'discoverProjectsByRegion', params: region.encodeAsLinkProperties()
//        List<Project> relevantProjects = projectService.relevantProjects(new Pagination(max:4))
//        List<Project> recommendedProjects = projectService.relevantProjects(new Pagination(max:5))
//        List<KuorumUser> mostActiveUsers = kuorumUserService.mostActiveUsersSince(new Date() -7, new Pagination(max:24))
////        List<KuorumUser> bestSponsors = kuorumUserService.bestSponsorsSince(new Date() -7, new Pagination(max:24))
//        List<KuorumUser> bestSponsors = kuorumUserService.mostActiveUsersSince(new Date() -365, new Pagination(max:24))
//        List<KuorumUser> bestPoliticians = kuorumUserService.bestPoliticiansSince(new Date() -7, new Pagination(max:32))
////        log.debug("discover")
//        [
//                relevantProjects:relevantProjects,
//                recommendedProjects:recommendedProjects,
//                mostActiveUsers:mostActiveUsers,
//                bestSponsors:bestSponsors,
//                bestPoliticians:bestPoliticians
//        ]
    }


    def customPostMapping(String customLink){
        Project project = Project.findByHashtag("#losDatosCuentan")
        redirect mapping: "projectShow" , params: project.encodeAsLinkProperties()
    }

    def customPostMappingEmpleoJuvenil(){
        Project project = Project.findByHashtag("#empleoJuvenil")
        redirect mapping: "projectShow" , params: project.encodeAsLinkProperties()
    }

    def customPostMappingSayNoToFracking(){
        Project project = Project.findByHashtag("#sayNoToFracking")
        redirect mapping: "projectShow" , params: project.encodeAsLinkProperties()
    }
    def customPostMappingImmigrationrc(){
        Project project = Project.findByHashtag("#immigrationRC")
        redirect mapping: "projectShow" , params: project.encodeAsLinkProperties()
    }

    def landingCitizens(){
        [command: new KuorumRegisterCommand()]
    }

    def landingPrices(){
        []
    }
}
