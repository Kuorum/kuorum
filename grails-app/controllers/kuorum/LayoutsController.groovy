package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser

class LayoutsController {

    def springSecurityService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHead() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHeadNoLinks(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }
}
