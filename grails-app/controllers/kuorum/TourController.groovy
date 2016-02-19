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
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO

class TourController {

    SpringSecurityService springSecurityService
    CausesService causesService
    SearchSolrService searchSolrService


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
        SearchParams searchPoliticiansPagination = new SearchParams(type: SolrType.POLITICIAN)
        SolrResults politicians = searchSolrService.search(searchPoliticiansPagination)
        render view: '/dashboard/dashboard', model:[
                loggedUser:user,
                causesSuggested:causesSuggested,
                causesPagination:causesPagination,
                politicians:politicians,
                searchPoliticiansPagination:searchPoliticiansPagination,
                tour:true
        ]

    }
}
