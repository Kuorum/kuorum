package kuorum

import com.opensymphony.module.sitemesh.RequestConstants
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchType
import kuorum.core.model.solr.SolrType
import kuorum.files.LessCompilerService
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.codehaus.groovy.grails.web.mapping.UrlCreator
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo
import org.codehaus.groovy.grails.web.mapping.UrlMappingsHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

import java.text.Normalizer

class NavigationTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']

    static namespace = "nav"

    def springSecurityService
    LinkGenerator grailsLinkGenerator
    LessCompilerService lessCompilerService

    /**
     * Returns the css "ACTIVE" if the mapping is the same as the url loaded
     */
    def activeMenuCss = { attrs ->
        String activeCss =  attrs.activeCss?:"active"
        def urlParams = attrs.urlParams?:[]
        List<String> mappings = []
        if (attrs.mappingName){
            mappings << attrs.mappingName
        }else{
            mappings = attrs.mappingNames
        }

        List<String> urls = mappings.collect{mappingName ->grailsLinkGenerator.link(mapping:mappingName, params: urlParams, absolute:false)}

        if (urls.contains(request.forwardURI.toString())){
            out << activeCss
        }
    }

    def ifActiveMapping = {attrs, body ->
        String mappingName = attrs.mappingName?:''
        List<String> mappingNames = attrs.mappingNames?attrs.mappingNames.split(",").collect{it.trim()}.findAll{it}:[mappingName]
        Boolean equals = attrs.equals?Boolean.parseBoolean(attrs.equals): true;
        List<String> urls = mappingNames.collect{grailsLinkGenerator.link(mapping:it, absolute: false)}

        // TODO: "request.getRequestURL()" is not "sign-in" at the sign-in page
        if (equals && urls.contains(request.getForwardURI().toString()) || !equals && !urls.contains(request.getForwardURI().toString())){
            out << body()
        }
    }

    def kuorumLink = {attrs, body ->
        Locale locale = org.springframework.context.i18n.LocaleContextHolder.getLocale()
        AvailableLanguage currentLang = AvailableLanguage.en_EN
        if (locale.language != "es"){
            // Kuorum Sales oly have es and en
            currentLang = AvailableLanguage.es_ES
        }

        out << """<a href="https://www.kuorum.org/${currentLang.locale.language}" hreflang="${currentLang.locale.language}" target="_blank">Kuorum.org</a>"""
    }

    /**
     * Language utils to display hreflangs and language selector
     */
    private  Map<AvailableLanguage, String> generateAllRelatedUrlsDependingOnLang() {
        // Init
        def languageList = [AvailableLanguage.es_ES, AvailableLanguage.en_EN]
        def urlPath = request.forwardURI.replaceFirst(request.contextPath, "")

        UrlMappingInfo urlMappingInfo = urlMappingsHolder.match(urlPath)
        String mappingNameParameter = "mappingName"
        Map<AvailableLanguage, String> urls = [:]
        if (urlMappingInfo && urlMappingInfo.parameters.containsKey(mappingNameParameter)){
            String mappingName = urlMappingInfo.parameters.get(mappingNameParameter)
            mappingName = prepareSpecialMappings(mappingName)
            def normalizedParams = [:]
            params.each {k, v ->
                if (v){
                    def normalizedValue = v
                    if (v instanceof String){
                        normalizedValue = Normalizer.normalize(v, Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    }
                    normalizedParams.put(
                            k,
                            normalizedValue
                )}
            }
            languageList.each { lang ->
                String link = g.createLink(mapping: "${lang.locale.language}_${mappingName}".toString(), params:normalizedParams, absolute: true);
                int dollarIndex = link.indexOf('?');
                if (dollarIndex != -1)
                {
                    link = link.substring(0, dollarIndex);
                }
                urls.put(lang, link)
            }
        }else if (urlMappingInfo){
            def parameters = [:]
            parameters << urlMappingInfo.parameters
            parameters.put("mappingName", "fake") // Fake to prevent a null pointer recovering urlCreator
            UrlCreator urlCreator = urlMappingsHolder.getReverseMapping(urlMappingInfo.controllerName, urlMappingInfo.actionName, parameters)
            languageList.each { lang ->
                parameters.put("lang", lang.getLocale().getLanguage())
                String url = urlCreator.createURL(parameters, null)
                urls.put(lang, url)
            }
        }
        return urls;
    }

    private String prepareSpecialMappings(String mapping){
        if (mapping.startsWith("searcherSearch")){
            mapping = "searcherSearch"
            SolrType solrType = SolrType.safeParse(params.type)
            if (solrType){
                mapping = "searcherSearch${solrType}"
            }

            if (params.searchType){
                SearchType searchType = SearchType.safeParse(params.searchType);
                if (!searchType){
                    // Google still asks for old filters
                    searchType = SearchType.ALL;
                }
                if (!SearchType.ALL.equals(searchType)){
                    // ONLY CAUSE IS MAPPED
                    mapping = mapping+"By"+searchType
                }
            }
        }
        return mapping;
    }

    @Autowired
    @Qualifier("grailsUrlMappingsHolder")
    UrlMappingsHolder urlMappingsHolder

    def canonical = {attrs, body ->
        Map<AvailableLanguage, String> urls= generateAllRelatedUrlsDependingOnLang();
        Locale locale = org.springframework.context.i18n.LocaleContextHolder.getLocale()
        AvailableLanguage currentLang = AvailableLanguage.fromLocale(locale)
        if (attrs.onlyLink){
            out << urls[currentLang]
        }else{
            out <<"<link rel='canonical' href='${urls[currentLang]}'/>"
        }
    }

    def generateAlternateLangLink = { attrs, body ->
        Map<AvailableLanguage, String> urls= generateAllRelatedUrlsDependingOnLang();
        urls.each {k, v->
            out << """
                <link   rel="alternate"
                        href="${v}"
                        hreflang="${k.locale.language}" />
            """
        }

//
//        // X-default
//        out << """
//            <link   rel="alternate"
//                    href="https://kuorum.org${request.forwardURI}"
//                    hreflang="x-default" />
//            """
    }

    def generateLangSelector = { attrs, body ->
        Map<AvailableLanguage, String> urls = generateAllRelatedUrlsDependingOnLang();
        Locale locale = org.springframework.context.i18n.LocaleContextHolder.getLocale()
        AvailableLanguage currentLang = AvailableLanguage.fromLocale(locale)
        out << """
<div class="dropdown">
  <a class="dropdown-toggle" type="button" id="dropdown-language-selector" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    ${g.message(code: "kuorum.core.model.AvailableLanguage.${currentLang}")}
    <span class="caret"></span>
  </a>
  </button>
  <ul class="dropdown-menu" aria-labelledby="dropdown-language-selector">
  """
        urls.each{k,v->
            // Check if lang is selected
            String languageName = g.message(code: "kuorum.core.model.AvailableLanguage.${k}")
            out << "<li><a href='${v}'>${languageName}</a></li>"
        }
        out << """
  </ul>
</div>
"""
    }

    def loadMoreLink = {attrs, body ->
        def numElements = attrs.numElements
        Pagination pagination = attrs.pagination
        String mapping = attrs.mapping
        def mappingParams = attrs.mappingParams?:[]
        String parentId = attrs.parentId
        String formId = attrs.formId?:''
        String cssClass = attrs.cssClass?:''
        String dataFormId = formId?"data-form-id='${formId}'":''
        String callback = attrs.callback?:''

        def link = createLink(mapping: mapping, params:mappingParams)
        if (numElements>=pagination.max){
            out <<"""
                <div class="load-more ${cssClass}">
                    <a href="${link}"
                        class="loadMore"
                        data-parent-id="${parentId}" ${dataFormId}
                        data-offset="${pagination.max}"
                        data-callback="${callback}">
                        ${message(code:"search.list.seeMore")}
                        <span class="fa fa-angle-down"></span>
                    </a>
                </div>
                """
        }
        out <<"""
    <div class="hidden">
        <form id="${formId}">
            <input type='hidden' name='max' value='${pagination.max}'/>
            """
        out << body()
        out <<"""
        </form>
    </div>
    """
    }

    def ifPageProperty={attrs, body ->
        String pageProperty = "page." + attrs.pageProperty
        String value = getPage().getProperty(pageProperty)
        if (!value || Boolean.parseBoolean(value)){
            out << body()
        }
    }

    def delayedSection ={attrs ->
        String divId = attrs.divId
        String reloadableClass=attrs.reload?"reload":""
        String urlLink = g.createLink(mapping: attrs.mapping, params: attrs.params)

        out <<
                """
            <div id='${divId}' class='delayed ${reloadableClass}' data-link='${urlLink}'>
            </div>
            """

    }

    protected getPage() {
        return getRequest().getAttribute(RequestConstants.PAGE)
    }

    def contactPagination={ attrs ->
        Long totalElements = attrs.total
        String ulClass= attrs.ulClasss
        Long currentPage = attrs.currentPage
        Long sizePage = attrs.sizePage
        Long totalPages = Math.floor(Math.max(totalElements-1,0) / sizePage)
        String link = attrs.link?:""

        out <<"<div class='pagination'>"
        out <<"<ul class='${ulClass}' data-page='${currentPage}' data-size='${sizePage}' data-link='${link}'>"

        Long lowerLimit = (currentPage +-1) * sizePage
        lowerLimit = Math.max(lowerLimit,0)
        String prevLink = link?"${link}${link.contains("?")?"&":"?"}offset=${lowerLimit}":""
        out << getPaginationLi("<", currentPage-1, currentPage == 0, prevLink)

        Long upperLimit = (currentPage +1) * sizePage
        upperLimit = Math.min(totalElements,upperLimit)
        String nextLink = link?"${link}${link.contains("?")?"&":"?"}offset=${upperLimit}":""
        out << getPaginationLi(">", currentPage+1, currentPage >= totalPages, nextLink )

        out <<"</ul>"

        out <<"<span class='counterList'>${(currentPage) * sizePage +1} - ${upperLimit} of <span class='totalList'>${totalElements}</span></span>"
        out << "</div>"
    }

    private String getPaginationLi(String arrow, Long nextPage, boolean disabled, String link="#"){
        """
            <li>
                <a class="page ${disabled?'disabled':''}" data-nextPage='${nextPage}' href="${disabled?'#':link}">${arrow}</a>
            </li>
        """
    }

    private String getPaginationDisabledLi(){
        """
            <li>
                <a class="page disabled" href="#">...</a>
            </li>
        """
    }
}
