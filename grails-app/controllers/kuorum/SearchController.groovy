package kuorum

import grails.converters.JSON
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.core.model.search.SearchParams
import kuorum.core.model.search.SuggestRegion
import kuorum.core.model.solr.*
import kuorum.web.constants.WebConstants
import org.springframework.web.servlet.LocaleResolver
import springSecurity.KuorumRegisterCommand

class SearchController{

    def searchSolrService
    def indexSolrService
    RegionService regionService
//    def messageSource
    LocaleResolver localeResolver
    def springSecurityService

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
            it.registerObjectMarshaller( SolrProject )   { SolrProject solrProject ->
                [
                    status:solrProject.subType,
                    title:solrProject.name,
                    hashtag:solrProject.hashtag,
                    url:g.createLink(mapping: 'projectShow', params:solrProject.encodeAsLinkProperties())
                ]
            }
            it.registerObjectMarshaller( SolrKuorumUser )   { SolrKuorumUser solrKuorumUser ->
                def urlImage = solrKuorumUser.urlImage
                if (!urlImage){
                    urlImage = g.resource(dir:'/images', file: 'user-default.jpg')
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

                solrAutocomplete.projects.each {
                    suggestions << [type:"PROJECT", value:it.name, data:it]
                }

                [suggestions:suggestions]
//                [
//                    suggests : solrAutocomplete.suggests,
//                    users: solrAutocomplete.kuorumUsers,
//                    projects: solrAutocomplete.projects
//                ]
            }
        }
    }

    def search(SearchParams searchParams) {
        Long maxElements = 10;
        SolrResults docs = searchDocs(searchParams, maxElements)
        [docs:docs, searchParams:searchParams]
    }
    def searchLanding(SearchParams searchParams){
        Long maxElements = 12;
        searchParams.type = SolrType.POLITICIAN
        SolrResults docs = searchDocs(searchParams, maxElements)
        if (docs.elements){
            render view:"searchLanding", model:[docs:docs, searchParams:searchParams]
        }else{
            render view:"searchLandingNoResults", model:[searchParams:searchParams, command: new KuorumRegisterCommand()]
        }
    }

    private SolrResults searchDocs(SearchParams searchParams, Long max){
        SolrResults docs
        if (searchParams.hasErrors()){
            searchParams=new SearchParams(word: '', type: searchParams.type?:SolrType.POLITICIAN)
            docs = searchSolrService.search(searchParams, searchParams.max)
        }else{
            Locale locale = localeResolver.resolveLocale(request)
            AvailableLanguage language = AvailableLanguage.fromLocaleParam(locale.language)
            Region suggestedRegion = regionService.findMostAccurateRegion(searchParams.word,null, language)
            List<Region> regions = []
            if (suggestedRegion){
               regions = regionService.findRegionsList(suggestedRegion)
            }
            if (regions){
                searchParams.boostedRegions = regions.collect{it.iso3166_2}
                searchParams.word= "${searchParams.word} ${regions.collect{it.iso3166_2.replace('-', '')}.join(" ")}"
            }
            searchParams.max = max
            docs = searchSolrService.search(searchParams)
            searchParams.word = params.word
        }
        return docs;
    }

    def modifyFilters(SearchParams searchParams) {
        SolrResults docs
        if (searchParams.hasErrors()){
            docs = new SolrResults(elements: [], numResults: 0, facets: [], suggest:null)
            searchParams.word=searchParams.word?:''
        }else{
            docs = searchSolrService.search(searchParams)
        }
        render template:'/search/searchElement', model:[docs:docs.elements]
    }

    def searchSeeMore(SearchParams searchParams){
        if (searchParams.hasErrors()){
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "false")
            render template: '/search/searchElement', model:[docs:[], searchParams:searchParams, columnsCss:params.columnsCss?:'']
        }else{
            SolrResults docs = searchDocs(searchParams, searchParams.max)
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${docs.numResults-searchParams.offset<=searchParams.max}")
            render template: '/search/searchElement', model:[docs:docs.elements, searchParams:searchParams, columnsCss:params.columnsCss?:'']
        }
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

    def suggestRegions(SuggestRegion suggestRegion){
        Locale locale = localeResolver.resolveLocale(request)
        AvailableLanguage language = AvailableLanguage.fromLocaleParam(locale.language)
        List<Region> regions = regionService.suggestRegions(suggestRegion.word, language)
        Map suggestions =[suggestions:regions.collect{[type:"SUGGESTION", value:it.name, data:it]}]
        render suggestions as JSON
    }

    def suggestTags(String term){
        SearchParams searchParams = new SearchParams(word:term)
        List<String> suggestions = searchSolrService.suggestTags(searchParams)
        render ([suggestions:suggestions] as JSON)
    }
}
