package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO

@Validateable
class ContestApplicationVoteCommand {

    String userAlias
    Long contestId
    Long campaignId

    static constraints = {
        userAlias nullable: false
        contestId nullable: false
        campaignId nullable: false
    }
}
