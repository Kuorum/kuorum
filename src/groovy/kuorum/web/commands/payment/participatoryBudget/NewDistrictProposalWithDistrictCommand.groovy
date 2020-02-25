package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand

@Validateable
class NewDistrictProposalWithDistrictCommand extends CampaignContentCommand{
    Long districtId
    String cause

    static constraints = {
        importFrom CampaignContentCommand
        districtId nullable: false
        cause nullable: false
    }

}
