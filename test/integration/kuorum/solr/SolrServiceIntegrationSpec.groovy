package kuorum.solr

import grails.test.spock.IntegrationSpec
import kuorum.core.model.CommissionType
import kuorum.core.model.search.SearchLaws
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.*
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
    def params = [// type is not needed
            [word:"parq",               subTypes:null],
            [word:"parques",            subTypes:null],
            [word:"parques na",         subTypes:null],
            [word:"parques na",         subTypes:SolrType.LAW.solrSubTypes],
            [word:"parques na",         subTypes:SolrType.POST.solrSubTypes],
            [word:"juanjo",             subTypes:null],
            [word:"juanjo a",           subTypes:SolrType.KUORUM_USER.solrSubTypes],
            [word:"juanjo a",           subTypes:[SolrSubType.ORGANIZATION]],
            [word:"juanjo a",           subTypes:[SolrSubType.ORGANIZATION]],
            [word:"#parquesna",         subTypes:null],
            [word:"#parquesnacionales", subTypes:null],
    ]
    def setup(){
        KuorumUser.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")
        indexSolrService.fullIndex()
    }

    @Unroll
    void "search on solr the : #params -> #quantity"() {
        given: "searh word ..."
            SearchParams searchParams = new SearchParams(params)
        when: "Searching"
            SolrResults results = searchSolrService.search(searchParams)
        then:
            results.numResults == quantity
            if (results.numResults){
                results.elements.collect{
                    it.name.contains(CSS_CLASS) || it?.text.contains(CSS_CLASS)
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
        params[10]          | 1             | 1       | 0        | null
            params[0]       | 0             | 0       | 0        | "parques"
            params[1]       | 1             | 1       | 0        | "parques"
            params[2]       | 1             | 1       | 0        | "parques nacionales"
            params[3]       | 1             | 1       | 0        | "parques nacionales"
            params[4]       | 1             | 0       | 0        | "parques nacionales"
            params[5]       | 1             | 0       | 1        | "juanjo alvite"
            params[6]       | 1             | 0       | 1        | "juanjo alvite"
            params[7]       | 0             | 0       | 0        | null
            params[8]       | 0             | 0       | 0        | null
            params[9]       | 0             | 0       | 0        | "#parquesnacionales"

    }

    @Unroll
    void "test list laws: #regionName, #commission "(){
        given:"Region"
        SearchLaws searchLaws = new SearchLaws(regionName:regionName, commissionType: commission)
        when:"search for "
        List<SolrLawsGrouped>  laws = searchSolrService.listLaws(searchLaws)
        then:
        laws.size() == numGroups
        where:
        regionName          | commission                | numGroups
        "espana"            | CommissionType.JUSTICE    | 2
        "espana"            | CommissionType.ECONOMY    | 1
        "espana"            | CommissionType.DEFENSE    | 0
        "espana"            | null                      | 2
        "Madrid"            | CommissionType.DEFENSE    | 0
        "Madrid"            | null                      | 1
    }
}
