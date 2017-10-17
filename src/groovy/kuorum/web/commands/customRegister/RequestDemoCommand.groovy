package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.ContactSectorType

/**
 * Form data for request a demo
 */
@Validateable
class RequestDemoCommand {

    String email
    String name
    String surname
    String enterprise
    String phone
    ContactSectorType sector
    String comment

    static constraints = {
        name nullable: false, maxSize: 15
        surname nullable: true
        email nullable:false, email:true
        enterprise nullable:false
        phone nullable:true
        sector nullable: true
        comment nullable: false, minSize: 10
    }
}
