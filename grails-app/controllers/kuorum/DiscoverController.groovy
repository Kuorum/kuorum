package kuorum

import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.law.LawService
import kuorum.post.PostService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.constants.WebConstants

class DiscoverController {

    LawService lawService

    KuorumUserService kuorumUserService

    PostService postService

    def discoverLaws() {
        Pagination pagination = new Pagination(max:10);
        List<Law> laws = lawService.recommendedLaws(new Pagination(max:10))
        [laws:laws, pagination: pagination]
    }

    def discoverLawsSeeMore(Pagination pagination) {
        List<Law> laws = lawService.recommendedLaws(pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${Law.count()-pagination.offset<=pagination.max}")
        render template: '/discover/discoverLawList', model:[laws:laws, pagination:pagination]
    }

    def discoverPosts() {

    }
    def discoverPostsSeeMore(Pagination pagination) {

    }
    def discoverPoliticians() {
        Pagination pagination = new Pagination(max:10);
        List<KuorumUser> politicians = kuorumUserService.bestPoliticiansSince(new Date() -7, pagination)
        [politicians:politicians, pagination: pagination]
    }

    def discoverPoliticiansSeeMore(Pagination pagination) {
        List<KuorumUser> politicians = kuorumUserService.bestPoliticiansSince(new Date() -7, pagination)
        //TODO: Si es multiplo de 10, hara un true cuando es false
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${politicians.size()-pagination.max!=0}")
        render template: '/discover/discoverUsersList', model:[users:politicians, pagination:pagination]
    }
}
