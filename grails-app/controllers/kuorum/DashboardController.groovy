package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.SolrResults
import kuorum.core.model.solr.SolrType
import kuorum.post.Cluck
import kuorum.project.Project
import kuorum.project.ProjectEvent
import kuorum.solr.SearchSolrService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserStatsService
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.kuorumUser.LeaningIndexRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
import springSecurity.KuorumRegisterCommand

class DashboardController {

    SpringSecurityService springSecurityService
    def cluckService
    def projectService
    def kuorumUserService
    KuorumUserStatsService kuorumUserStatsService
    CausesService causesService
    SearchSolrService searchSolrService

    private  static final Integer MAX_PROJECT_EVENTS = 2

    def index(){
        if (springSecurityService.isLoggedIn()){
//            render(view: "dashboard", model: dashboard())
            redirect (mapping:"dashboard")
        }else{
            render(view: "landingPage", model: landingPage())
            //redirect (mapping:"landingPage")
        }
    }
    def dashboard() {
        if (!springSecurityService.isLoggedIn()){
            redirect(mapping:"home")
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        if (SpringSecurityUtils.ifAnyGranted("ROLE_POLITICIAN")){
            return politicianDashboard(user)
        }else{
            return userDashboard(user)
        }
        Pagination pagination = new Pagination()
        List<Cluck> clucks =  cluckService.dashboardClucks(user,pagination)
        List<ProjectEvent> projectEvents = projectService.findRelevantProjectEvents(user, new Pagination(max: MAX_PROJECT_EVENTS))
        List<KuorumUser>mostActiveUsers=[]
        if (!clucks){
            mostActiveUsers = kuorumUserService.mostActiveUsersSince(new Date() -7 , new Pagination(max: 20))
        }
        [clucks: splitClucksInParts(clucks), projectEvents:projectEvents,mostActiveUsers:mostActiveUsers, user:user,seeMore: clucks.size()==pagination.max]
    }

    def politicianDashboard(KuorumUser user){
        render view: 'politicianDashboard', model:[user:user]
    }

    def userDashboard(KuorumUser user){
        Pagination causesPagination = new Pagination(max:6)
        SuggestedCausesRSDTO causesSuggested = causesService.suggestCauses(user, causesPagination)
        SearchParams searchPoliticiansPagination = new SearchParams(type: SolrType.POLITICIAN)
        SolrResults politicians = searchSolrService.search(searchPoliticiansPagination)
        render view: 'userDashboard', model:[
                loggedUser:user,
                causesSuggested:causesSuggested,
                causesPagination:causesPagination,
                politicians:politicians,
                searchPoliticiansPagination:searchPoliticiansPagination
        ]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardCauses(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        SuggestedCausesRSDTO causesSuggested = causesService.suggestCauses(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${causesSuggested.total <= pagination.offset}")
        render template: "/dashboard/dashboardModules/causeCardList", model:[causes:causesSuggested.data]
    }

    private def splitClucksInParts(List<Cluck> clucks){
        if (clucks && clucks.size() > 2){
            return [clucks_1:clucks[0..1], clucks_2:clucks[2..clucks.size()-1]]
        }
        return [clucks_1:clucks, clucks_2:[]]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardClucks(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ProjectEvent> projectEvents = projectService.findRelevantProjectEvents(user, new Pagination(max: MAX_PROJECT_EVENTS, offset: pagination.offset))
        List<Cluck> clucks =  cluckService.dashboardClucks(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<pagination.max}")
        render template: "/cluck/liClucks", model:[clucks:clucks, projectEvents:projectEvents]
    }

    def landingPage(){
        if (springSecurityService.isLoggedIn()){
            redirect(mapping:"home")
            return
        }
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

    def landingEditors(){
        [command: new KuorumRegisterCommand()]
    }

    def landingPrices(){
        []
    }
}
