package kuorum

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
}
