package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.Pagination
import kuorum.post.Cluck
import kuorum.users.KuorumUser
import kuorum.web.constants.WebConstants
import springSecurity.KuorumRegisterCommand

class DashboardController {

    def springSecurityService
    def cluckService
    def lawService
    def kuorumUserService

    def index(){
        if (springSecurityService.isLoggedIn()){
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
        List<KuorumUser>mostActiveUsers=[]
        if (!clucks){
            mostActiveUsers = kuorumUserService.mostActiveUsersSince(new Date() -7 , new Pagination(max: 20))
        }
        [clucks: clucks,mostActiveUsers:mostActiveUsers, user:user,seeMore: clucks.size()==pagination.max]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardClucks(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<Cluck> clucks =  cluckService.dashboardClucks(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<pagination.max}")
        render template: "/cluck/liClucks", model:[clucks:clucks]
    }

    def landingPage(){
        if (springSecurityService.isLoggedIn()){
            redirect(mapping:"home")
            return
        }
        [command: new KuorumRegisterCommand()]
    }

    def discover(){
        redirect mapping:'discoverLaws'
//        List<Law> relevantLaws = lawService.relevantLaws(new Pagination(max:4))
//        List<Law> recommendedLaws = lawService.relevantLaws(new Pagination(max:5))
//        List<KuorumUser> mostActiveUsers = kuorumUserService.mostActiveUsersSince(new Date() -7, new Pagination(max:24))
////        List<KuorumUser> bestSponsors = kuorumUserService.bestSponsorsSince(new Date() -7, new Pagination(max:24))
//        List<KuorumUser> bestSponsors = kuorumUserService.mostActiveUsersSince(new Date() -365, new Pagination(max:24))
//        List<KuorumUser> bestPoliticians = kuorumUserService.bestPoliticiansSince(new Date() -7, new Pagination(max:32))
////        log.debug("discover")
//        [
//                relevantLaws:relevantLaws,
//                recommendedLaws:recommendedLaws,
//                mostActiveUsers:mostActiveUsers,
//                bestSponsors:bestSponsors,
//                bestPoliticians:bestPoliticians
//        ]
    }
}
