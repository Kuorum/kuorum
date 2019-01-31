package kuorum.solr

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService

@Transactional
class IndexSolrService {

    RestKuorumApiService restKuorumApiService

    SpringSecurityService springSecurityService

    def fullIndex() {
        KuorumUserSession user = springSecurityService.principal
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.SEARCH_INDEX_FULL,
                [:],
                [email:user.getEmail(), userName:user.name],
                null)
        return 0
    }


    void deltaIndex(){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.SEARCH_INDEX_DELTA,
                [:],
                [:],
                null)
    }
}
