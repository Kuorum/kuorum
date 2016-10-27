package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.core.model.CommissionType
import kuorum.mail.MailGroupType
import kuorum.mail.MailType

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class MailNotificationsCommand {

    List<MailGroupType> availableMails

    static constraints = {
        availableMails nullable: true
    }
}
