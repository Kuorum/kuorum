package kuorum.solr

import kuorum.users.KuorumUser
import org.springframework.security.core.userdetails.UserDetails
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 2/02/14.
 */
class SolrServiceIntegrationSpec extends Specification{

    def indexSolrService
    def searchSolrService

    def setup(){
        indexSolrService.fullIndex()
    }


    @Unroll
    void "search on solr the : #word -> #quantity"() {
        given: "searh worf ..."
        def wordToSearchFor = word
        expect: "Quantity found correct"
        def founded = searchSolrService.search(wordToSearchFor)
        where: "Username with params...."
        quantity || word
        KuorumUser.collection.count() || '*'
        1 || "Peter"
        1 || 'peter'
    }
}
