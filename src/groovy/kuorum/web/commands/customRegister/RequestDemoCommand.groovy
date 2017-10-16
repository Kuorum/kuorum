package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.Sector

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
    Sector sector
    String comment

    static constraints = {
        name nullable: false, maxSize: 15
        surname nullable: true
        email nullable:false, email:true
        enterprise nullable:false
        phone nullable:true
        sector nullable: false
        comment nullable: false, minSize: 10
    }
}
