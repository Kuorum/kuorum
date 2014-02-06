package kuorum

import grails.converters.JSON

class SearchController {
    def searchSolrService
    def indexSolrService

    def search(String word) {

        def docs = searchSolrService.search(word)
        render docs as JSON

    }

    def index(){
         indexSolrService.fullIndex()
        render "Ok"
    }

    def suggest(String word){
        def res = searchSolrService.suggest(word)
        render res as JSON
    }
}
