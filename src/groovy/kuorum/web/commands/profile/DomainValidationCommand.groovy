package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class DomainValidationCommand {

    final Date OLDER_THAN_YEAR_16 = Calendar.instance.with { add( YEAR, -16 ) ; it }.time

    String ndi
    String postalCode
    Date birthDate

    static constraints = {
        ndi nullable:false
        postalCode nullable:false
        birthDate nullable:false, max: OLDER_THAN_YEAR_16
    }
}
