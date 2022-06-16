package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO
import org.kuorum.rest.model.communication.participatoryBudget.BackerTypeRSDTO
import org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO

@Validateable
class NewContestApplicationCommand extends CampaignContentCommand {
    Long contestId
    String cause
    ContestApplicationActivityTypeDTO activityType
    ContestApplicationFocusTypeDTO focusType

    static constraints = {
        importFrom CampaignContentCommand
        contestId nullable: false
        cause nullable: false
        activityType nullable: false
        focusType nullable: false
    }

}
