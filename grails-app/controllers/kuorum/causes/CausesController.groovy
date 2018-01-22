package kuorum.causes

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.Pagination
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserStatsService
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
import org.kuorum.rest.model.tag.SupportedCauseRSDTO

class CausesController {

    CausesService causesService

    SpringSecurityService springSecurityService;

    KuorumUserStatsService kuorumUserStatsService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def supportCause(String causeName) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        SupportedCauseRSDTO causeRSDTO = causesService.toggleSupportCause(user, causeName)
        render ([cause:causeRSDTO] as JSON)
    }

}
