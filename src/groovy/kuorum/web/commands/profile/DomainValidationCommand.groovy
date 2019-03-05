package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class DomainValidationCommand {

    String ndi
    String postalCode
    Date birthDate

    static constraints = {
        ndi nullable:false
        postalCode nullable:false
        birthDate nullable:false, validator: {val ->
            if (val.after(Calendar.instance.with { add( YEAR, -16 ) ; it }.time)){
                return "max.error"
            }
        }

    }
}
