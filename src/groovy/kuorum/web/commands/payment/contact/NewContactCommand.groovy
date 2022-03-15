package kuorum.web.commands.payment.contact

import grails.validation.Validateable

/**
 * Created by iduetxe on 1/09/16.
 */
@Validateable
class NewContactCommand {
    String name;
    String surname;
    String email;
    Long phone;
    Boolean conditions;

    static constraints = {
        name nullable: false
        surname nullable: true
        phone nullable: true
        email nullable: false, email: true // Is nullable because when the contact is a followe we don't have the email
        conditions nullable: false
    }
}
