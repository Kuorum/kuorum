package kuorum

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
        String mappingName = attrs.mappingName
        String url = grailsLinkGenerator.link(mapping:mappingName,absolute: true)
        if (request.getRequestURL().toString() == url){
            out << "active"
        }


    }


    def ifActiveMapping = {attrs, body ->
        String mappingName = attrs.mappingName
        String url = grailsLinkGenerator.link(mapping:mappingName,absolute: true)
        if (request.getRequestURL().toString() == url){
            out << body()
        }
    }

    def loadMoreLink = {attrs, body ->
        def numElements = attrs.numElements
        Pagination pagination = attrs.pagination
        String mapping = attrs.mapping
        def mappingParams = attrs.mappingParams?:[]
        String parentId = attrs.parentId
        String formId = attrs.formId?:''
        String cssClass = attrs.class?:''
        String dataFormId = formId?"data-form-id='${formId}'":''

        def link = createLink(mapping: mapping, params:mappingParams)
        if (numElements>=pagination.max){
            out <<"""
                <div id="load-more" class="text-center ${cssClass}">
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
