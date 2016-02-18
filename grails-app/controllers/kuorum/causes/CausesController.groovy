package kuorum.causes

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.search.Pagination
import kuorum.users.KuorumUser
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
import org.kuorum.rest.model.tag.SupportedCauseRSDTO

class CausesController {

    CausesService causesService

    SpringSecurityService springSecurityService;

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def supportCause(String causeName) {
        KuorumUser user = springSecurityService.currentUser
        SupportedCauseRSDTO causeRSDTO = causesService.toggleSupportCause(user, causeName)
        render causeRSDTO as JSON
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def discardCause(String causeName){
        KuorumUser user = springSecurityService.currentUser
        causesService.discardSuggestedCause(user, causeName)
        Pagination pagination = new Pagination(offset: params.offset, max: 1)
        SuggestedCausesRSDTO causesSuggested = causesService.suggestCauses(user, pagination)
        render template: "/dashboard/dashboardModules/causeCardList", model:[causes:causesSuggested.data]
    }
}
