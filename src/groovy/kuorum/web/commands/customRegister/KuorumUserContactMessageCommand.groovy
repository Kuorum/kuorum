package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 4/12/15.
 */
@Validateable
class KuorumUserContactMessageCommand {
    String cause
    String message
    KuorumUser politician

    static constraints = {
        message nullable: false, minSize: 10
        cause nullable: false
        politician nullable:false
    }
}
