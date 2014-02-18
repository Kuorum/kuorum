package kuorum.solr

import grails.test.spock.IntegrationSpec
import kuorum.core.model.solr.SolrAutocomplete
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser
import org.springframework.security.core.userdetails.UserDetails
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 2/02/14.
 */
class SolrServiceIntegrationSpec extends IntegrationSpec{

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
        KuorumUser.collection.count() + Post.collection.count() + Law.collection.count()|| '*'
        1 || "Peter"
        1 || 'peter'
    }

    @Unroll
    void "check suggestions: #word "(){
        given:"A word to search"
            def wordToSearchFor = word
        when:"search for "
            SolrAutocomplete solrAutocomplete = searchSolrService.suggest(wordToSearchFor)
        then:
            solrAutocomplete.numResults == numResults
            solrAutocomplete.laws.size() == numLaws
            solrAutocomplete.kuorumUsers.size() == numUsers
            if (solrAutocomplete.suggests){
                solrAutocomplete.suggests[0] == firstSuggest
            }
        where:
            word        | numResults    | numLaws | numUsers | firstSuggest
            "parq"      | 0             | 0       | 0        | "parques"
            "parques"   | 3             | 1       | 0        | "parques"
            "parques na"| 3             | 1       | 0        | "parques nacionales"
            "juanjo"    | 1             | 0       | 1        | "juanjo"
            "juanjo a"  | 1             | 0       | 1        | "juanjo alvite"
    }
}
