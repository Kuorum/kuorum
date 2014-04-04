package kuorum.solr

import grails.test.spock.IntegrationSpec
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.SolrAutocomplete
import kuorum.core.model.solr.SolrResults
import kuorum.core.model.solr.SolrSubType
import kuorum.core.model.solr.SolrType
import kuorum.users.KuorumUser
import spock.lang.Shared
import spock.lang.Unroll

/**
 * Created by iduetxe on 2/02/14.
 */
class SolrServiceIntegrationSpec extends IntegrationSpec{

    def indexSolrService
    def searchSolrService
    def fixtureLoader

    //CSS class highligted from solr
    private static final CSS_CLASS = "highlighted"

    @Shared
    def params = [
            [word:"parq",       type:null,                  subType:null],
            [word:"parques",    type:null,                  subType:null],
            [word:"parques na", type:null,                  subType:null],
            [word:"parques na", type:SolrType.LAW,          subType:null],
            [word:"parques na", type:SolrType.POST,         subType:null],
            [word:"juanjo",     type:null,                  subType:null],
            [word:"juanjo a",   type:SolrType.KUORUM_USER,  subType:null],
            [word:"juanjo a",   type:SolrType.KUORUM_USER,  subType:SolrSubType.ORGANIZATION],
            [word:"juanjo a",   type:SolrType.LAW,          subType:SolrSubType.ORGANIZATION]
    ]
    def setup(){
        KuorumUser.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")
        indexSolrService.fullIndex()
    }

    @Unroll
    void "search on solr the : #params -> #quantity"() {
        given: "searh worf ..."
            SearchParams searchParams = new SearchParams(params)
        when: "Search for"
            SolrResults results = searchSolrService.search(searchParams)
        then:
            results.numResults == quantity
            if (results.numResults){
                results.elements.collect{
                    it.name.contains(CSS_CLASS) || it?.text.contains(CSS_CLASS) || it?.owner.contains(CSS_CLASS)
                }.find{it}
            }
            results.facets.size() > 0
            facets.collect{id,val->
                results.facets.find{it.facetName==id}.hits == val
            }.find{it}

        where: "Username with params...."
            params      | quantity  | facets
            params[0]   | 0         | ["${SolrType.KUORUM_USER}":0,"${SolrType.LAW}":0, "${SolrType.POST}":0]
            params[1]   | 3         | ["${SolrType.KUORUM_USER}":0,"${SolrType.LAW}":1, "${SolrType.POST}":2,"${SolrSubType.PURPOSE}":2]
            params[3]   | 1         | ["${SolrType.KUORUM_USER}":0,"${SolrType.LAW}":1, "${SolrType.POST}":0,"${SolrSubType.PURPOSE}":0]
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
            params          | numResults    | numLaws | numUsers | firstSuggest
            params[0]       | 0             | 0       | 0        | "parques"
            params[1]       | 3             | 1       | 0        | "parques"
            params[2]       | 3             | 1       | 0        | "parques nacionales"
            params[3]       | 1             | 1       | 0        | "parques nacionales"
            params[4]       | 2             | 0       | 0        | "parques nacionales"
            params[5]       | 1             | 0       | 1        | "juanjo alvite"
            params[6]       | 1             | 0       | 1        | "juanjo alvite"
            params[7]       | 0             | 0       | 0        | null
            params[8]       | 0             | 0       | 0        | null
    }
}
