package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat

@Validateable
class ContestDeadlinesCommand {
    Long campaignId
    Date deadLineApplications
    Date deadLineReview
    Date deadLineVotes
    Date deadLineResults
    Integer numWinnerApplications;

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType

    static constraints = {
        importFrom CampaignContentCommand, include: ["publishOn", "sendType"]
        campaignId nullable: false
        deadLineApplications nullable: false
        deadLineReview nullable: false
        deadLineVotes nullable: false
        deadLineResults nullable: false
        numWinnerApplications nullable: false
    }

}
