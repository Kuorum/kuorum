package kuorum

import com.opensymphony.module.sitemesh.RequestConstants
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.search.Pagination
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

class NavigationTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']

    static namespace = "nav"

    def springSecurityService
    LinkGenerator grailsLinkGenerator

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

        List<String> urls = mappings.collect{mappingName ->grailsLinkGenerator.link(mapping:mappingName, params: urlParams)}

        if (urls.contains(request.forwardURI.toString())){
            out << activeCss
        }
    }

    def ifActiveMapping = {attrs, body ->
        String mappingName = attrs.mappingName?:''
        List<String> mappingNames = attrs.mappingNames?attrs.mappingNames.split(",").collect{it.trim()}.findAll{it}:[mappingName]
        Boolean equals = attrs.equals?Boolean.parseBoolean(attrs.equals): true;
        List<String> urls = mappingNames.collect{grailsLinkGenerator.link(mapping:it)}

        // TODO: "request.getRequestURL()" is not "sign-in" at the sign-in page
        if (equals && urls.contains(request.getForwardURI().toString()) || !equals && !urls.contains(request.getForwardURI().toString())){
            out << body()
        }
    }

    /**
     * Language utils to display hreflangs and language selector
     */
    private getLanguageUtils() {
        // Init
        def languageList = AvailableLanguage.values()
        def urlPath = request.forwardURI.replaceFirst(request.contextPath, "")

        // Lang at start (landing) or in the middle (other)
        // Don't confuse with user profile, example: "/ESperanzaaguirre"
        StringBuilder str = new StringBuilder("")
        languageList.each { lang ->
            if (str.length() > 0) str.append('|')
            str.append(lang.locale.language)
        }
        def languagesRegex = str.toString()
        def langAtStartRegex = /^(\/($languagesRegex))$/
        def langAtMiddleRegex = /^(\/($languagesRegex)\/)/
        def isUsingPathLanguages = (urlPath =~ langAtStartRegex || urlPath =~ langAtMiddleRegex)

        def usingLanguagesList = new ArrayList<Boolean>()
        def languageUrlList = new ArrayList<String>()
        languageList.each { lang ->
            usingLanguagesList.add(
                    urlPath =~ /^\/$lang.locale.language$/ || urlPath =~ /^\/$lang.locale.language\//
            )

            String link;
            params;
            if (isUsingPathLanguages) {
                // Using "/es" or "/es/"
                link = request.contextPath + urlPath
                        .replaceFirst(langAtStartRegex, "/" + lang.locale.language)
                        .replaceFirst(langAtMiddleRegex, "/" + lang.locale.language + "/") +
                        (request.queryString ? "?" + request.queryString : "");
            } else if (request.getParameter("lang") != null && !request.getParameter("lang").isEmpty()) {
                // Using "?lang=es" or "&lang=es"
                link = request.contextPath + urlPath
                        .replaceFirst(langAtStartRegex, "/" + lang.locale.language)
                        .replaceFirst(langAtMiddleRegex, "/" + lang.locale.language + "/") +
                        ("?" + request.queryString.replace("lang=" + request.getParameter("lang"), "lang=" + lang.locale.language));
            } else {
                // Not using any language
                link = request.contextPath + urlPath
                        .replaceFirst(langAtStartRegex, "/" + lang.locale.language)
                        .replaceFirst(langAtMiddleRegex, "/" + lang.locale.language + "/") +
                        (request.queryString ?
                                "?" + request.queryString + "&lang=" + lang.locale.language
                                : "?lang=" + lang.locale.language);
            }
            languageUrlList.add(link)
        }

        return [
            // es, en, it, ...
            "languageList": languageList,
            // If using "/$lang/" then true
            "isUsingPathLanguages": isUsingPathLanguages,
            // true, false, false, ...
            "usingLanguagesList": usingLanguagesList,
            // "/kuorum/es", "/kuorum/en", "/kuorum/it", ...
            "languageUrlList": languageUrlList,
        ];
    }

    def generateAlternateLangLink = { attrs, body ->
        def languages = getLanguageUtils();

        for (int i = 0; i < languages["languageList"].size(); i++) {
            out << """
                    <link   rel="alternate"
                            href="https://kuorum.org${languages["languageUrlList"][i]}"
                            hreflang="${languages["languageList"][i].locale.language}" />
                """
        }

        // X-default
        out << """
            <link   rel="alternate"
                    href="https://kuorum.org${request.forwardURI}"
                    hreflang="x-default" />
            """
    }

    def generateLangSelector = { attrs, body ->
        def languages = getLanguageUtils();

        out << "<select id='language-selector'>"
        for (int i = 0; i < languages["languageList"].size(); i++) {
            // Check if lang is selected
            String languageName = g.message(code: "kuorum.core.model.AvailableLanguage.${languages["languageList"][i]}")
            out << "<option ${languages["usingLanguagesList"][i]?'selected':''} value='${languages["languageUrlList"][i]}'>${languageName}</option>"
        }
        out << "</select>"
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
        Long totalPages = Math.floor(totalElements / sizePage)
        String link = attrs.link?:"#"

        out <<"<ul class='${ulClass}' data-page='${currentPage}' data-size='${sizePage}' data-link='${link}'>"

        out << getPaginationLi("<", currentPage-1, currentPage == 0)
        out << getPaginationLi(">", currentPage+1, currentPage >= totalPages )

        out <<"</ul>"
        Long upperLimit = (currentPage +1) * sizePage
        upperLimit = upperLimit>totalElements?totalElements:upperLimit
        out <<"<span class='counterList'>${(currentPage) * sizePage +1} - ${upperLimit} of <span class='totalList'>${totalElements}</span></span>"
    }

    private String getPaginationLi(String arrow, Long nextPage, boolean disabled){
        """
            <li>
                <a class="page ${disabled?'disabled':''}" data-nextPage='${nextPage}' href="#">${arrow}</a>
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
