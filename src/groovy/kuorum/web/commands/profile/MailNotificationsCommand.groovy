package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.mail.MailType
import springSecurity.RegisterController

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class MailNotificationsCommand {

    List<MailType> availableMails

    static constraints = {
    }
}
