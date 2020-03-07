package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class DomainUserPhoneValidationCommand {

    String phoneNumberPrefix
    Long phoneNumber

    static constraints = {
        phoneNumberPrefix nullable:false
        phoneNumber nullable:false
    }

    public String getCompletePhoneNumber(){
        return "${phoneNumberPrefix}${phoneNumber}"
    }
}
