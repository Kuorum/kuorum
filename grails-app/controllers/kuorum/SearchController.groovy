package kuorum

import grails.converters.JSON
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.core.model.search.SearchParams
import kuorum.core.model.search.SearchType
import kuorum.core.model.search.SuggestRegion
import kuorum.core.model.solr.*
import kuorum.users.KuorumUser
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

//    @PostConstruct
    void init() {
        JSON.createNamedConfig('suggest') {
//            log("suggest JSON marshaled created")
            it.registerObjectMarshaller( SolrType)          { SolrType solrType             -> messageEnumJson(solrType)}
            it.registerObjectMarshaller( CommissionType )   { CommissionType commissionType -> messageEnumJson(commissionType)}
            it.registerObjectMarshaller( SolrKuorumUser )   { SolrKuorumUser solrKuorumUser ->
                def urlImage = solrKuorumUser.urlImage
                if (!urlImage){
                    urlImage = g.resource(dir:'/images', file: 'user-default.jpg')
                }
                [
                    name:solrKuorumUser.name,
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
        SolrResults docs = searchDocs(searchParams, params)
        [docs:docs, searchParams:searchParams]
    }
    def searchLanding(SearchParams searchParams){
        if (springSecurityService.isLoggedIn()){
            redirect (mapping:"dashboard")
            return;
        }

        searchParams.type = SolrType.KUORUM_USER
        searchParams.max = 12;
        SolrResults docs = searchDocs(searchParams, params)
        if (docs.elements){
            render view:"searchLanding", model:[docs:docs, searchParams:searchParams]
        }else{
            render view:"searchLandingNoResults", model:[searchParams:searchParams, command: new KuorumRegisterCommand()]
        }
    }

    private SolrResults searchDocs(SearchParams searchParams, def params){
        SolrResults docs
        String regionCountryCode = request.session.getAttribute(WebConstants.COUNTRY_CODE_SESSION)
        if (searchParams.hasErrors()){
            searchParams=new SearchParams(word: '', type: searchParams.type?:SolrType.KUORUM_USER)
            docs = searchSolrService.search(searchParams)
        }else{
            searchParams.searchType = searchParams.searchType?:SearchType.ALL
            searchParams = createRegionSearchParams(searchParams, params, regionCountryCode)
            docs = searchSolrService.search(searchParams)
        }
        return docs;
    }

    private SearchParams createRegionSearchParams( SearchParams searchParams, def params, String preferredCountry){
        SearchParams editedSearchParams = searchParams
//        if (searchParams.searchType== SearchType.ALL && !params.word && !params.regionCode){
//            // NO PARAMS -> Show politicians from country
//            editedSearchParams =  editedSearchParams = new SearchParams(searchParams.properties)
//            editedSearchParams.searchType = SearchType.REGION
//            Region country = Region.findByIso3166_2(preferredCountry)
//            List<Region> regions= regionService.findRegionsList(country)
//            editedSearchParams.boostedRegions = regions.collect{it.iso3166_2}
//            editedSearchParams.word = "";
//        }else
        if (searchParams.searchType == SearchType.REGION || searchParams.searchType== SearchType.ALL){
            editedSearchParams = new SearchParams(searchParams.properties)
            Locale locale = localeResolver.resolveLocale(request)
            AvailableLanguage language = AvailableLanguage.fromLocaleParam(locale.language)
            Region suggestedRegion = null;
            if (params.regionCode && searchParams.searchType==SearchType.REGION){
                suggestedRegion = regionService.findRegionBySuggestedId(params.regionCode)
            }
            if (!suggestedRegion){
                suggestedRegion = regionService.findMostAccurateRegion(searchParams.word,null, language)
            }

            List<Region> regions = []
            if (suggestedRegion){
                regions = regionService.findRegionsList(suggestedRegion)
            }
            if (regions){
//                editedSearchParams.regionIsoCodes = regions.collect{it.iso3166_2}
                editedSearchParams.boostedRegions = regions.collect{it.iso3166_2}
                editedSearchParams.word = "";
            }
        }
        if (searchParams.searchType== SearchType.ALL){
            // Restoring word to search
            editedSearchParams.word = searchParams.word
        }
        return editedSearchParams;
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
            SolrResults docs = searchDocs(searchParams, params)
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${docs.numResults-searchParams.offset<=searchParams.max}")
            render template: '/search/searchElement', model:[docs:docs.elements, searchParams:searchParams, columnsCss:params.columnsCss?:'']
        }
    }


    def suggest(SearchParams searchParams){
        if (searchParams.hasErrors()){
            render ([] as JSON)
            return;
        }
        if (searchParams.searchType == SearchType.REGION){
            return suggestRegions(new SuggestRegion(word: searchParams.word))
        }else{
            SolrAutocomplete res = searchSolrService.suggest(searchParams)
            init()
            JSON.use('suggest') {
                render res as JSON
            }
        }
    }

    def suggestRegions(SuggestRegion suggestRegion){
        Locale locale = localeResolver.resolveLocale(request)
        AvailableLanguage language = AvailableLanguage.fromLocaleParam(locale.language)
        List<Region> regions = regionService.suggestRegions(suggestRegion.word, language)
        Map suggestions =[suggestions:regions.collect{[type:"REGION", value:it.name, data:it]}]
        render suggestions as JSON
    }

    def suggestTags(String term){
        SearchParams searchParams = new SearchParams(word:term)
        List<String> suggestions = searchSolrService.suggestTags(searchParams)
        render ([suggestions:suggestions] as JSON)
    }

    def suggestAlias(String term){
        List<String> boostedAlias = params.list('boostedAlias[]')
        List<String> aliasFriends = []
        if (springSecurityService.isLoggedIn()){
            aliasFriends = ((KuorumUser)springSecurityService.currentUser).following.collect{KuorumUser.get(it)}.findAll{it}.collect{it.alias}
        }
//        term = term.replaceAll("[^\\x00-\\x7F]", "") // The mention plugin sends a weird character at the end
        def suggestions = searchSolrService.suggestAlias(term, boostedAlias,aliasFriends)
        suggestions = suggestions.collect{s->[
                alias:s.alias,
                name:s.name,
                avatar:s.avatar?:g.resource(dir:'images', file: 'user-default.jpg'),
                link:g.createLink(mapping: 'userShow', params: [userAlias:s.alias], absolute:true)
        ]}
        render ([suggestions:suggestions] as JSON)
    }
}
