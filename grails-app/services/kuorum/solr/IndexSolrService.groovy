package kuorum.solr

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService

@Transactional
class IndexSolrService {

    RestKuorumApiService restKuorumApiService

    SpringSecurityService springSecurityService;

    def fullIndex() {
        KuorumUser user = springSecurityService.currentUser
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.SEARCH_INDEX_FULL,
                [:],
                [email:user.getEmail(), userName:user.fullName],
                null)
        return 0;
    }


    void deltaIndex(){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.SEARCH_INDEX_DELTA,
                [:],
                [:],
                null)
    }
}
