package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO

@Validateable
class ContestApplicationChangeStatusCommand {

    Long contestApplicationId
    CampaignStatusRSDTO newStatus

    static constraints = {
        contestApplicationId nullable: false
        newStatus nullable: false
    }
}
