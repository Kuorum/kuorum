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
        email nullable: true, email: true
        conditions nullable: false
    }
}
