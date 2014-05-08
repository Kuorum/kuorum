package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.Pagination
import kuorum.law.Law
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
        List<Cluck> clucks =  cluckService.dashboardClucks(user)
        [clucks: clucks, user:user]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardClucks(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<Cluck> clucks =  cluckService.dashboardClucks(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<=pagination.max}")
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
        List<Law> relevantLaws = lawService.relevantLaws(new Pagination(max:4))
        List<Law> recommendedLaws = lawService.recommendedLaws(new Pagination(max:4))
        List<KuorumUser> mostActiveUsers = kuorumUserService.mostActiveUsersSince(new Date() -7, new Pagination(max:20))
//        log.debug("discover")
        [relevantLaws:relevantLaws, recommendedLaws:recommendedLaws,mostActiveUsers:mostActiveUsers]
    }
}
