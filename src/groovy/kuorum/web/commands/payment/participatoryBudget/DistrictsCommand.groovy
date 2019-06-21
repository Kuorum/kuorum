package kuorum.web.commands.payment.participatoryBudget

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat

@Validateable
class DistrictsCommand {
    Long campaignId

    List<DistrictCommand> districts = []

    Integer maxDistrictProposalsPerUser
    Integer minVotesImplementProposals


    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType

    static constraints = {
        importFrom CampaignContentCommand, include: ["publishOn", "sendType"]
        districts minSize: 1, maxSize: 100
        maxDistrictProposalsPerUser min: 1
        minVotesImplementProposals min: 0
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