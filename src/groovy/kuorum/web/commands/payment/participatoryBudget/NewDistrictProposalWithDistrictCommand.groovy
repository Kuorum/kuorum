package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import org.kuorum.rest.model.communication.participatoryBudget.BackerTypeRSDTO
import org.kuorum.rest.model.communication.survey.CampaignVisibilityRSDTO

@Validateable
class NewDistrictProposalWithDistrictCommand extends CampaignContentCommand{
    Long districtId
    String cause
    CampaignVisibilityRSDTO campaignVisibility = CampaignVisibilityRSDTO.VISIBLE
    BackerTypeRSDTO backerType

    static constraints = {
        importFrom CampaignContentCommand
        districtId nullable: false
        cause nullable: false
        backerType nullable: false
    }

}
