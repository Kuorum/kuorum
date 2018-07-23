package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO

@Validateable
class ParticipatoryBudgetChangeStatusCommand {

    ParticipatoryBudgetChangeStatusCommand() {}

    ParticipatoryBudgetChangeStatusCommand(ParticipatoryBudgetRSDTO participatoryBudgetRSDTO) {
        this.status = participatoryBudgetRSDTO.status
        this.campaignId = participatoryBudgetRSDTO.id
    }
    Long campaignId
    ParticipatoryBudgetStatusDTO status;

    static constraints = {
        campaignId nullable: false
        status nullable: false
    }
}
