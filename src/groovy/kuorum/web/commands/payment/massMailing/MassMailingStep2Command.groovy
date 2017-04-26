package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import org.kuorum.rest.model.notification.campaign.CampaignTemplateDTO

/**
 * Created by toni on 21/4/17.
 */
@Validateable
class MassMailingStep2Command {

    CampaignTemplateDTO contentType

    static constraints = {
        contentType nullable: false
    }
}
