package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class ChangeEmailCommand {

    String email

    static constraints = {
        email nullable: false, blank: false, email: true
    }
}
