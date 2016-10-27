package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 22/05/14.
 */
@Validateable
class DeleteAccountCommand {
    String explanation
    Boolean forever

    static constraints = {
        explanation nullable: false, minSize: 10
        forever nullable:true
    }
}
