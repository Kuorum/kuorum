package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class DomainUserCustomCodeValidationCommand {

    String customCode

    static constraints = {
        customCode nullable:false
    }
}
