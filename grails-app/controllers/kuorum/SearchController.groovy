package kuorum

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.CommissionType
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.*
import kuorum.web.constants.WebConstants

class SearchController{

    def searchSolrService
    def indexSolrService
//    def messageSource

    private messageEnumJson(def type){
        [
                type:type.toString(),
//                i18n:messageSource.getMessage("${type.class.name}.${type}")
                i18n:g.message(code: "${type.class.name}.${type}")
        ]
    }

   private userRoleJson(SolrKuorumUser user){
       def type = user.role.toString()
       def i18n = g.message(code: "${user.role.class.name}.${user.role}.${user.gender}")
       if (user.subType == SolrSubType.POLITICIAN){
           type = user.subType.toString()
           i18n = g.message(code: "${user.subType.class.name}.${user.subType}")
       }
//       if (user.gender)
        [
            type:type,
            i18n:i18n
        ]
   }

//    @PostConstruct
    void init() {
        JSON.createNamedConfig('suggest') {
//            log("suggest JSON marshaled created")
            it.registerObjectMarshaller( SolrType)          { SolrType solrType             -> messageEnumJson(solrType)}
            it.registerObjectMarshaller( SolrSubType)       { SolrSubType solrSubType       -> messageEnumJson(solrSubType)}
            it.registerObjectMarshaller( CommissionType )   { CommissionType commissionType -> messageEnumJson(commissionType)}
            it.registerObjectMarshaller( SolrLaw )   { SolrLaw solrLaw ->
                [
                    status:solrLaw.subType,
                    title:solrLaw.name,
                    hashtag:solrLaw.hashtag,
                    url:g.createLink(mapping: 'lawShow', params:solrLaw.encodeAsLinkProperties())
                ]
            }
            it.registerObjectMarshaller( SolrKuorumUser )   { SolrKuorumUser solrKuorumUser ->
                def urlImage = solrKuorumUser.urlImage
                if (!urlImage){
                    urlImage = g.resource(dir:'/images', file: 'pre-user.jpg')
                }
                [
                    name:solrKuorumUser.name,
                    role:userRoleJson(solrKuorumUser),
                    urlAvatar:urlImage,
                    url:g.createLink(mapping: 'userShow', params:solrKuorumUser.encodeAsLinkProperties())
                ]
            }
            it.registerObjectMarshaller(SolrAutocomplete){SolrAutocomplete solrAutocomplete ->
               def suggestions = []

                solrAutocomplete.suggests.each {
                    suggestions  << [type:"SUGGESTION", value:it, data:it]
                }
                solrAutocomplete.kuorumUsers.each {
                    suggestions << [type:"USER", value:it.name, data:it]
                }

                solrAutocomplete.laws.each {
                    suggestions << [type:"LAW", value:it.name, data:it]
                }

                [suggestions:suggestions]
//                [
//                    suggests : solrAutocomplete.suggests,
//                    users: solrAutocomplete.kuorumUsers,
//                    laws: solrAutocomplete.laws
//                ]
            }
        }
    }

    def search(SearchParams searchParams) {
        SolrResults docs
        if (searchParams.hasErrors()){
            docs = new SolrResults(elements: [], numResults: 0, facets: [], suggest:null)
            searchParams.word=searchParams.word?:''
        }else{
            if (searchParams.type){
                searchParams.subTypes += searchParams.type.solrSubTypes
            }
            if (!searchParams.subTypes){
                searchParams.subTypes = SolrSubType.values()
            }
            docs = searchSolrService.search(searchParams)
        }
        [docs:docs, searchParams:searchParams]
    }

    def modifyFilters(SearchParams searchParams) {
        SolrResults docs
        if (searchParams.hasErrors() || !searchParams.subTypes){
            docs = new SolrResults(elements: [], numResults: 0, facets: [], suggest:null)
            searchParams.word=searchParams.word?:''
        }else{
            docs = searchSolrService.search(searchParams)
        }
        render template:'/search/searchElement', model:[docs:docs.elements]
    }

    def searchSeeMore(SearchParams searchParams){
        searchParams.word = params.wordOrg
        searchParams.validate()
        SolrResults docs = searchSolrService.search(searchParams)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${docs.elements.size()<=searchParams.max}")
        render template: '/search/searchElement', model:[docs:docs.elements, searchParams:searchParams]
    }

    @Secured(['ROLE_ADMIN'])
    def fullIndex(){
         indexSolrService.fullIndex()
        render "Ok"
    }

    def suggest(SearchParams searchParams){
        if (searchParams.hasErrors()){
            render ([] as JSON)
            return;
        }
        SolrAutocomplete res = searchSolrService.suggest(searchParams)
        init()
        JSON.use('suggest') {
            render res as JSON
        }
    }
}
