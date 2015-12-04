package kuorum.web.commands.customRegister

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/12/15.
 */
@Validateable
class ContactRegister {
    String name;
    String cause;
    String message;
    String email;

    static constraints = {
        email nullable: false, email:true
        name nullable:false
        message nullable: false, minSize: 10
        cause nullable: false
    }
}
