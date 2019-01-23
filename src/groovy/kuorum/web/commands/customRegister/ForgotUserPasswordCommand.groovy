package kuorum.web.commands.customRegister

import grails.validation.Validateable

/**
 * Created with IntelliJ IDEA.
 * User: iduetxe
 * Date: 14/08/13
 * Time: 19:28
 * To change this template use File | Settings | File Templates.
 */
@Validateable
class ForgotUserPasswordCommand {

    String email

    static constraints = {
        email nullable: false, email:true
    }

}
