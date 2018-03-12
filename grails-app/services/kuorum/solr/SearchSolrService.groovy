package kuorum.solr

import com.fasterxml.jackson.core.type.TypeReference
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.search.SearchParams
import kuorum.core.model.search.SearchType
import kuorum.core.model.solr.SolrAutocomplete
import kuorum.util.rest.RestKuorumApiService
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrRequest
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.params.CommonParams
import org.kuorum.rest.model.search.SearchByRDTO
import org.kuorum.rest.model.search.SearchParamsRDTO
import org.kuorum.rest.model.search.SearchResultsRSDTO
import org.kuorum.rest.model.search.SearchTypeRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO

@Transactional(readOnly = true)
class SearchSolrService {

    SolrServer server
    IndexSolrService indexSolrService
    RestKuorumApiService restKuorumApiService
    SpringSecurityService springSecurityService

    SearchResultsRSDTO searchAPI(SearchParams searchParams){
        SearchParamsRDTO searchParamsRDTO = convertParams(searchParams)
        Map<String, String> query = searchParamsRDTO.encodeAsQueryParams()
        if (springSecurityService.loggedIn){
            query.put("viewerUid", springSecurityService.currentUser.id)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.SEARCH,
                [:],
                query,
                new TypeReference<SearchResultsRSDTO>(){})
        SearchResultsRSDTO resultsRSDTO = new SearchResultsRSDTO();
        if (response.data){
            resultsRSDTO = response.data
        }
        resultsRSDTO

    }

    private SearchParamsRDTO convertParams(SearchParams searchParams){
        SearchParamsRDTO searchParamsRDTO = new SearchParamsRDTO()
        searchParamsRDTO.searchBy = SearchByRDTO.valueOf(searchParams.getSearchType().toString()) // CHAPU
        searchParamsRDTO.type = searchParams.getType()?SearchTypeRSDTO.valueOf(searchParams.getType().toString()):null // CHAPU
        searchParamsRDTO.boostedRegions = searchParams.boostedRegions
        searchParamsRDTO.filteredIds = searchParams.filteredUserIds
        searchParamsRDTO.boostedAlias = searchParams.boostedAlias
        searchParamsRDTO.word = searchParams.word
        searchParamsRDTO.page= Math.round(searchParams.offset/searchParams.max)
        searchParamsRDTO.size= searchParams.max
        searchParamsRDTO
    }

    SolrAutocomplete suggest(SearchParams params){

        Map<String, String> query = [word:params.word]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.SEARCH_SUGGEST,
                [:],
                query,
                new TypeReference<List<String>>(){})
        SolrAutocomplete solrAutocomplete = new SolrAutocomplete(suggests:response.data)
        solrAutocomplete
    }
//
//
//    private void prepareScore(SearchParams params, SolrQuery query){
//        switch (params.searchType){
//            case SearchType.REGION:
//                query.setParam("qf","regionIso3166_2^10.0 constituencyIso3166_2^5.0")
//                break;
//            case SearchType.CAUSE:
//                query.setParam("qf","tags^10.0")
//                break;
//            case SearchType.ALL:
//                break;
//            default:
//            //DEFAULT
//                break;
//        }
//    }
//    private void prepareBoosted(SearchParams params, SolrQuery query){
//        if (params.boostedRegions){
////            String boost = params.boostedRegions.collect{"regionIso3166_2:${it.replace('-', '')}^${Math.pow(it.length(),2)}"}.join(" ")
////            boost = "${boost} name^1000 hashtag^800.0"
////            query.setParam("bq", boost)
//            query.setParam("bf","ord(kuorumRelevance)^1 ord(relevance)^50 ord(regionIso3166_2Length)^100")
//        }else{
//            query.setParam("bf","ord(kuorumRelevance)^1 ord(relevance)^50")
//        }
//    }

    private void prepareWord(SearchParams params, SolrQuery query){
        String word = params.word
        if (params.boostedRegions){
            word = "${word} regionIso3166_2:${params.boostedRegions.collect{it.replace('-', '')}.join(" regionIso3166_2:")}"
        }
        if (!word){
            word = "*"
        }
        if (params.searchType == SearchType.CAUSE){
            word = "\"${word}\""
        }
        query.setParam(CommonParams.Q, word);
    }

    private void prepareFilter(SearchParams params, SolrQuery query){
        StringBuffer filterQuery = new StringBuffer("");
        String separator = ""
        if (params.type){
            filterQuery.append(separator)
            filterQuery.append("type:(${params.type})")
            separator = " AND "
        }

        if (params.filteredUserIds){
            filterQuery.append(separator)
            filterQuery.append("-id:(${params.filteredUserIds.join(" ")})")
            separator = " AND "
        }

        if (filterQuery.length()){
            query.setParam(CommonParams.FQ, filterQuery.toString())
        }
    }

    private ArrayList<String> prepareAutocompleteSuggestions(QueryResponse rsp){
//        def collations = rsp._spellInfo.suggestions.getAll("collation")
//        ArrayList<String> suggests = new ArrayList<String>(collations.size())
//        if (collations.size()>0){
//            rsp._spellInfo.suggestions.getAll("collation").each{
//                if (it.hits >  0){
//                    suggests.add(it.misspellingsAndCorrections.first().value)
//                }
//            }
//        }
//        suggests
        if (rsp?._spellInfo?.suggestions && rsp._spellInfo.suggestions.size()>0){
            rsp._spellInfo.suggestions.first().value.suggestion
        }else{
            []
        }

    }

    public List<String> suggestTags(SearchParams params){
        if (!params.validate()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(params,"Parametros de búsqueda erroneos")
        }
        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/suggestTags");
        query.setParam("spellcheck.q", params.word);
        //query.setParam(TermsParams.TERMS_FIELD, "name", "username");
        prepareWord(params,query)
        prepareFilter(params, query)
        query.setParam("facet.prefix",params.word)
        QueryResponse rsp = server.query( query, SolrRequest.METHOD.POST );
        List<String> suggestions = prepareAutocompleteSuggestions(rsp)
        suggestions
    }

    /**
     * Returns a map of alias and names
     * @param search
     * @param boostedAlias
     * @return
     */
    def suggestAlias(String search, List<String> boostedAlias){

        SearchParamsRDTO searchParamsRDTO = new SearchParamsRDTO();
        searchParamsRDTO.boostedAlias = boostedAlias
        searchParamsRDTO.word = search
        Map<String, String> query = searchParamsRDTO.encodeAsQueryParams()
        if (springSecurityService.loggedIn){
            query.put("viewerUid", springSecurityService.currentUser.id)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.SEARCH_SUGGEST_USERS,
                [:],
                query,
                new TypeReference<List<SearchKuorumUserRSDTO>>(){})

        def suggestions = response.data.collect{[alias:it.alias, name:it.name, avatar:it.urlImage]}
        suggestions
    }
}
