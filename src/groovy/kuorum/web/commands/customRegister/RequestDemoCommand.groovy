package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.OfferType
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand
import springSecurity.KuorumRegisterCommand

/**
 * Form data for request a demo
 */
@Validateable
class RequestDemoCommand {

    String email
    String name
    String enterprise
    String phone

    static constraints = {
        name nullable: false, maxSize: 15
        email nullable:false, email:true
        enterprise nullable:false
        phone nullable:true
    }
}
