package kuorum.web.commands.payment.contact

import grails.validation.Validateable

/**
 * Created by iduetxe on 1/09/16.
 */
@Validateable
class ContactCommand {
    String name;
    String email;

    static constraints = {
        name nullable: false
        email nullable: false, email: true
    }
}
