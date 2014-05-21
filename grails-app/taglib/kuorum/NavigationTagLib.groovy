package kuorum

import kuorum.core.model.search.Pagination

class NavigationTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']

    static namespace = "nav"

    def springSecurityService

    def ACTIVE_ACTIONIS=[
            [mappingName: "discover", controller:"dashboard", action:"discover", onlyLogged:false],
            [mappingName: "home", controller:null, action:null, onlyLogged:true], // TODO: Chapu. Including an action in a gsp, the controller and the action are null.
            [mappingName: "footerWhatIsKuorum", controller:"footer", action:"whatIsKuorum", onlyLogged:false]
    ]

    /**
     * Returns the css "ACTIVE" if the page
     */
    def activeMenuCss = { attrs ->
        String mappingName = attrs.mappingName
        def activeMenu = ACTIVE_ACTIONIS.find{it.controller == params.controller && it.action == params.action}
        if (activeMenu && mappingName == activeMenu.mappingName){
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
