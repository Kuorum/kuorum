package kuorum

import grails.converters.JSON
import kuorum.core.model.search.SearchParams

class SearchController {
    def searchSolrService
    def indexSolrService

    def search(String word) {

        SearchParams searchParams = new SearchParams(word:word)
        def docs = searchSolrService.search(searchParams)
        render docs as JSON

    }

    def index(){
         indexSolrService.fullIndex()
        render "Ok"
    }

    def suggest(String word){
        SearchParams searchParams = new SearchParams(word:word)
        def res = searchSolrService.suggest(searchParams)
        render res as JSON
    }
}
