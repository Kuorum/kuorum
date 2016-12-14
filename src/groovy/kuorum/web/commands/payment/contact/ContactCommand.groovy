package kuorum.web.commands.payment.contact

import grails.validation.Validateable

/**
 * Created by iduetxe on 1/09/16.
 */
@Validateable
class ContactCommand {
    Long contactId;
    String name;
    String surname;
    String email;

    static constraints = {
        contactId nullable: false
        name nullable: false
        surname nullable: true
        email nullable: true, email: true // Is nullable because when the contact is a followe we don't have the email
    }
}
