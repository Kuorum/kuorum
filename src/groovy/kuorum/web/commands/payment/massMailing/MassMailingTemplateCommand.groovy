package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import org.kuorum.rest.model.communication.bulletin.BulletinNewsletterTemplateDTO
import org.kuorum.rest.model.notification.campaign.NewsletterTemplateDTO

/**
 * Created by toni on 21/4/17.
 */
@Validateable
class MassMailingTemplateCommand {

    BulletinNewsletterTemplateDTO contentType

    static constraints = {
        contentType nullable: false
    }
}
