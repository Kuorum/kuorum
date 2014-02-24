package kuorum

import kuorum.post.Cluck
import kuorum.users.KuorumUser

class DashboardController {

    def springSecurityService
    def cluckService

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
        KuorumUser user = KuorumUser.findById(springSecurityService.principal.id)
        List<Cluck> clucks =  cluckService.dashboardClucks(user)
        [clucks: clucks]
    }

    def landingPage(){
        if (springSecurityService.isLoggedIn()){
            redirect(mapping:"home")
            return
        }
    }
}
