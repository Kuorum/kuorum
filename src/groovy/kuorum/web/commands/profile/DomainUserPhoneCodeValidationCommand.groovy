package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class DomainUserPhoneCodeValidationCommand {

    String completePhoneNumber
    String phoneHash
    String phoneCode

    static constraints = {
        completePhoneNumber nullable:false
        phoneHash nullable:false
        phoneCode nullable:false
    }
}
