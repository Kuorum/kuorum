package kuorum.solr

import com.fasterxml.jackson.core.type.TypeReference
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.SolrAutocomplete
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.search.SearchByRDTO
import org.kuorum.rest.model.search.SearchParamsRDTO
import org.kuorum.rest.model.search.SearchResultsRSDTO
import org.kuorum.rest.model.search.SearchTypeRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO

@Transactional(readOnly = true)
class SearchSolrService {

    RestKuorumApiService restKuorumApiService
    SpringSecurityService springSecurityService

    SearchResultsRSDTO searchAPI(SearchParams searchParams){
        SearchParamsRDTO searchParamsRDTO = convertParams(searchParams)
        return searchAPI(searchParamsRDTO);
    }
    SearchResultsRSDTO searchAPI(SearchParamsRDTO searchParamsRDTO){

//        Map<String, String> query = searchParamsRDTO.encodeAsQueryParams()
        Map<String, String> query = [:]
        if (springSecurityService.loggedIn){
            query.put("viewerUid", springSecurityService.principal.id.toString())
        }
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.SEARCH,
                [:],
                query,
                searchParamsRDTO,
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
        searchParamsRDTO.addType(searchParams.getSolrType()?SearchTypeRSDTO.valueOf(searchParams.getSolrType().toString()):null) // CHAPU
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


    List<String> suggestTags(SearchParams params){
        if (!params.validate()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(params,"Parametros de búsqueda erroneos")
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.SEARCH_SUGGEST_CAUSES,
                [:],
                [prefix:params.word],
                new TypeReference<List<String>>(){})
        response.data
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
//        Map<String, String> query = searchParamsRDTO.encodeAsQueryParams()
        Map<String, String> query = [:]
        if (springSecurityService.loggedIn){
            query.put("viewerUid", springSecurityService.principal.id)
        }
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.SEARCH_SUGGEST_USERS,
                [:],
                query,
                searchParamsRDTO,
                new TypeReference<List<SearchKuorumUserRSDTO>>(){})

        def suggestions = response.data.collect{[alias:it.alias, name:it.name, avatar:it.urlImage]}
        suggestions
    }
}
