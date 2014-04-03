package kuorum

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.SearchParams

class SearchController {
    def searchSolrService
    def indexSolrService

    def search(SearchParams searchParams) {

        def docs = searchSolrService.search(searchParams)
        [docs:docs]

    }

    @Secured(['ROLE_ADMIN'])
    def fullIndex(){
         indexSolrService.fullIndex()
        render "Ok"
    }

    def suggest(String word){
        SearchParams searchParams = new SearchParams(word:word)
        def res = searchSolrService.suggest(searchParams)
        render res as JSON
    }
}
