package kuorum.web.commands.customRegister

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/12/15.
 */
@Validateable
class KuorumUserContactMessageCommand {
    String cause
    String message
    String contactUserId

    static constraints = {
        message nullable: false, minSize: 10
        cause nullable: false
        contactUserId nullable:false
    }
}
