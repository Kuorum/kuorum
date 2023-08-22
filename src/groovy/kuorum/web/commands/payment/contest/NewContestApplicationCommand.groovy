package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO
import org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO
import org.kuorum.rest.model.communication.participatoryBudget.BackerTypeRSDTO
import org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO

@Validateable
class NewContestApplicationCommand extends CampaignContentCommand {
    String name
    Long contestId

    static constraints = {
        contestId nullable: false
        name nullable: false
        body nullable: false
        headerPictureId nullable: false
    }

}