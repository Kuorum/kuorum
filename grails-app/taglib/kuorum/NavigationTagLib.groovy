package kuorum

import kuorum.core.model.search.Pagination
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

class NavigationTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']

    static namespace = "nav"

    def springSecurityService
    LinkGenerator grailsLinkGenerator

    def ACTIVE_ACTIONIS=[
            [mappingName: "discover",  onlyLogged:false],
            [mappingName: "discoverProjects",  onlyLogged:false],
            [mappingName: "discoverPoliticians",  onlyLogged:false],
            [mappingName: "discoverRecentPosts",  onlyLogged:false],
            [mappingName: "discoverRecommendedPosts",  onlyLogged:false],
            [mappingName: "funnelSuccessfulStories",  onlyLogged:false],
            [mappingName: "home", onlyLogged:true],
            [mappingName: "footerWhatIsKuorum", onlyLogged:false]
    ]

    /**
     * Returns the css "ACTIVE" if the page
     */
    def activeMenuCss = { attrs ->
        String mappingName = attrs.mappingName
        String url = grailsLinkGenerator.link(mapping:mappingName,absolute: true)
        def activeMenu = ACTIVE_ACTIONIS.find{it.mappingName == mappingName}
        if (request.getRequestURL().toString() == url){
            if (!activeMenu.logged || activeMenu.logged && springSecurityService.isLoggedIn()){
                out << "active"
            }
        }


    }

    def loadMoreLink = {attrs, body ->
        def numElements = attrs.numElements
        Pagination pagination = attrs.pagination
        String mapping = attrs.mapping
        def mappingParams = attrs.mappingParams?:[]
        String parentId = attrs.parentId
        String formId = attrs.formId?:''
        String dataFormId = formId?"data-form-id='${formId}'":''

        def link = createLink(mapping: mapping, params:mappingParams)
        if (numElements>=pagination.max){
            out <<"""
                <div id="load-more" class="text-center">
                    <a href="${link}" class="loadMore" data-parent-id="${parentId}" ${dataFormId}>
                        ${message(code:"search.list.seeMore")}
                    </a>
                </div>
                """
        }
        out <<"""
    <div class="hidden">
        <form id="${formId}">
            """
        out << body()
        out <<"""
        </form>
    </div>
    """
    }
}
