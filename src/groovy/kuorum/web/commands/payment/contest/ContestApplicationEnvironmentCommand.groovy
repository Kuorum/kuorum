package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO
import org.kuorum.rest.model.communication.participatoryBudget.BackerTypeRSDTO

@Validateable
class ContestApplicationEnvironmentCommand {
    String name
    String cause
    ContestApplicationActivityTypeDTO activityType
    ContestApplicationFocusTypeDTO focusType

    static constraints = {
        name nullable: false, maxSize: 100
        cause nullable: false
        activityType nullable: false
        focusType nullable: false
    }

}
