package kuorum.web.commands.customRegister

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/12/15.
 */
@Validateable
class KuorumUserContactMessageCommand {
    String subject
    String cause
    String message
    String contactUserId

    static constraints = {
        subject nullable: false
        message nullable: false, minSize: 1
        cause nullable: true
        contactUserId nullable:false
    }
}
