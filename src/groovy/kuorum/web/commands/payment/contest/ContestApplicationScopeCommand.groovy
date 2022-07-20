package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO
import org.kuorum.rest.model.communication.participatoryBudget.BackerTypeRSDTO

@Validateable
class ContestApplicationScopeCommand {
    String name
    String cause
    ContestApplicationActivityTypeDTO activityType
    ContestApplicationFocusTypeDTO focusType

    static constraints = {
        name nullable: false
        cause nullable: false
        activityType nullable: false
        focusType nullable: false
    }

}
