package kuorum

import com.opensymphony.module.sitemesh.RequestConstants
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
        String mappingName = attrs.mappingName?:''
        List<String> mappingNames = attrs.mappingNames?attrs.mappingNames.split(",").collect{it.trim()}.findAll{it}:[mappingName]
        Boolean equals = attrs.equals?Boolean.parseBoolean(attrs.equals): true;
        List<String> urls = mappingNames.collect{grailsLinkGenerator.link(mapping:it,absolute: true)}

        if (equals && urls.contains(request.getRequestURL().toString()) || !equals && !urls.contains(request.getRequestURL().toString())){
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
        String cssClass = attrs.cssClass?:''
        String dataFormId = formId?"data-form-id='${formId}'":''

        def link = createLink(mapping: mapping, params:mappingParams)
        if (numElements>=pagination.max){
            out <<"""
                <div class="load-more ${cssClass}">
                    <a href="${link}" class="loadMore" data-parent-id="${parentId}" ${dataFormId} data-offset="${pagination.max}">
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
}
