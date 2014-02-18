package kuorum.solr

import grails.test.spock.IntegrationSpec
import kuorum.core.model.solr.SearchParams
import kuorum.core.model.solr.SolrAutocomplete
import kuorum.core.model.solr.SolrSubType
import kuorum.core.model.solr.SolrType
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
    void "check suggestions: #params "(){
        given:"A word to search"
            SearchParams searchParams = new SearchParams(params)
        when:"search for "
            SolrAutocomplete solrAutocomplete = searchSolrService.suggest(searchParams)
        then:
            solrAutocomplete.numResults == numResults
            solrAutocomplete.laws.size() == numLaws
            solrAutocomplete.kuorumUsers.size() == numUsers
            if (solrAutocomplete.suggests){
                solrAutocomplete.suggests[0] == firstSuggest
            }
        where:
            params                                                                                | numResults    | numLaws | numUsers | firstSuggest
            [word:"parq",       type:null,                  subType:null]                         | 0             | 0       | 0        | "parques"
            [word:"parques",    type:null,                  subType:null]                         | 3             | 1       | 0        | "parques"
            [word:"parques na", type:null,                  subType:null]                         | 3             | 1       | 0        | "parques nacionales"
            [word:"parques na", type:SolrType.LAW,          subType:null]                         | 1             | 1       | 0        | "parques nacionales"
            [word:"parques na", type:SolrType.POST,         subType:null]                         | 2             | 0       | 0        | "parques nacionales"
            [word:"juanjo",     type:null,                  subType:null]                         | 1             | 0       | 1        | "juanjo"
            [word:"juanjo a",   type:SolrType.KUORUM_USER,  subType:null]                         | 1             | 0       | 1        | "juanjo alvite"
            [word:"juanjo a",   type:SolrType.KUORUM_USER,  subType:SolrSubType.ORGANIZATION]     | 0             | 0       | 0        | null
            [word:"juanjo a",   type:SolrType.LAW,          subType:SolrSubType.ORGANIZATION]     | 0             | 0       | 0        | null
    }
}
