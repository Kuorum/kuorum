package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat

@Validateable
class ContestDeadlinesCommand {
    Long campaignId
    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date deadLineApplications
    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date deadLineReview
    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date deadLineVotes
    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date deadLineResults

    static constraints = {
        campaignId nullable: false
        deadLineApplications nullable: false
        deadLineReview nullable: false
        deadLineVotes nullable: false
        deadLineResults nullable: false
    }

}
