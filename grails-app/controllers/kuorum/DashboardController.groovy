package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.Pagination
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.project.Project
import kuorum.project.ProjectEvent
import kuorum.users.KuorumUser
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId
import springSecurity.KuorumRegisterCommand

class DashboardController {

    def springSecurityService
    def cluckService
    def projectService
    def kuorumUserService

    private  static final Integer MAX_PROJECT_EVENTS = 2

    def index(){
        if (springSecurityService.isLoggedIn()){
            //TODO: Al ser el cálculo de la actividad de usuarios una operación lenta, habrá que ver dónde ponerlo para que no penalice el tiempo
//          KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
//          kuorumUserService.recommendedUsersByActivityAndUser(user)
            render(view: "dashboard", model: dashboard())
            //redirect (mapping:"dashboard")
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
        Pagination pagination = new Pagination()
        List<Cluck> clucks =  cluckService.dashboardClucks(user,pagination)
        List<ProjectEvent> projectEvents = projectService.findRelevantProjectEvents(user, new Pagination(max: MAX_PROJECT_EVENTS))
        List<KuorumUser>mostActiveUsers=[]
        if (!clucks){
            mostActiveUsers = kuorumUserService.mostActiveUsersSince(new Date() -7 , new Pagination(max: 20))
        }
        [clucks: splitClucksInParts(clucks), projectEvents:projectEvents,mostActiveUsers:mostActiveUsers, user:user,seeMore: clucks.size()==pagination.max]
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
}
