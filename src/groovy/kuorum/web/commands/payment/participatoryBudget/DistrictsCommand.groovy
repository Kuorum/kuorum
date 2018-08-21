package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat

@Validateable
class DistrictsCommand {
    Long campaignId

    Date deadLineProposals
    Date deadLineTechnicalReview
    Date deadLineVotes
    Date deadLineResults

    List<DistrictCommand> districts = []


    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType

    static constraints = {
        importFrom CampaignContentCommand, include: ["publishOn", "sendType"]
        districts minSize: 1, maxSize: 100
        deadLineProposals nullable: false
        deadLineTechnicalReview nullable: false
        deadLineVotes nullable: false
        deadLineResults nullable: false
    }

}

@Validateable
class DistrictCommand{
    Long districtId
    String name
    Long budget
    Boolean allCity

    static constraints = {
        districtId nullable: true
        name nullable: false, blank: false
        budget nullable: false, min:0L
        allCity nullable: true
    }
}