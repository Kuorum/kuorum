package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class DomainUserPhoneCodeValidationCommand {

    String validationPhoneNumberPrefix
    String validationPhoneNumber
    String phoneHash
    String phoneCode

    static constraints = {
        validationPhoneNumberPrefix nullable:false
        validationPhoneNumber nullable:false
        phoneHash nullable:false
        phoneCode nullable:false
    }
}
