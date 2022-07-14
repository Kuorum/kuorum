package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import org.kuorum.rest.model.communication.contest.ContestRSDTO
import org.kuorum.rest.model.communication.contest.ContestStatusDTO

@Validateable
class ContestChangeStatusCommand {
    ContestChangeStatusCommand() {}

    ContestChangeStatusCommand(ContestRSDTO contestRSDTO) {
        this.status = contestRSDTO.status
        this.campaignId = contestRSDTO.id
    }
    Long campaignId
    ContestStatusDTO status;

    static constraints = {
        campaignId nullable: false
        status nullable: false
    }
}
