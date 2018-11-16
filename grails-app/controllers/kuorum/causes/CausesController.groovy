package kuorum.causes

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.register.KuorumUserSession
import org.kuorum.rest.model.tag.SupportedCauseRSDTO

class CausesController {

    CausesService causesService

    SpringSecurityService springSecurityService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def supportCause(String causeName) {
        KuorumUserSession user = springSecurityService.principal
        SupportedCauseRSDTO causeRSDTO = causesService.toggleSupportCause(user, causeName)
        render ([cause:causeRSDTO] as JSON)
    }

}
