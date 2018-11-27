package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO

class TourController {

    SpringSecurityService springSecurityService
    CausesService causesService
    KuorumUserService kuorumUserService


    def index(){
        redirect(mapping:'tour_dashboard')
    }

    def tour_dashboard() {
        if (!springSecurityService.isLoggedIn()){
            redirect(mapping:"home")
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Pagination causesPagination = new Pagination(max:6)
        SuggestedCausesRSDTO causesSuggested = causesService.suggestCauses(user, causesPagination)
        Pagination politiciansDashboardPagination = new Pagination(max:6)
        List<SearchKuorumUserRSDTO> politicians = kuorumUserService.recommendUsers(user,politiciansDashboardPagination)
        render view: '/dashboard/dashboard', model:[
                loggedUser:user,
                causesSuggested:causesSuggested,
                causesPagination:causesPagination,
                recommendations:politicians,
                politiciansDashboardPagination:politiciansDashboardPagination,
                tour:true
        ]
    }
}
