package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.causes.CausesService
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.SolrResults
import kuorum.core.model.solr.SolrType
import kuorum.project.Project
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.solr.SearchSolrService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
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
        List<KuorumUser> politicians = kuorumUserService.recommendPoliticians(user,politiciansDashboardPagination)
        render view: '/dashboard/dashboard', model:[
                loggedUser:user,
                causesSuggested:causesSuggested,
                causesPagination:causesPagination,
                politicians:politicians,
                politiciansDashboardPagination:politiciansDashboardPagination,
                tour:true
        ]
    }
}
