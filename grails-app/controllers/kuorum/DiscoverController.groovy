package kuorum

import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.law.LawService
import kuorum.web.constants.WebConstants

class DiscoverController {

    LawService lawService

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

    def discoverPosts() {}

    def discoverPoliticians() {}
}
