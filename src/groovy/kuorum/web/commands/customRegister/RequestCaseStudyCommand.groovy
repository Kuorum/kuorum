package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.EnterpriseSector

/**
 * Form data for request a demo
 */
@Validateable
class RequestCaseStudyCommand {

    String email
    String name

    static constraints = {
        name nullable: false
        email nullable:false, email:true
    }
}
