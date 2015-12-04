package kuorum.web.commands.customRegister

import grails.plugin.springsecurity.ui.RegisterController
import grails.validation.Validateable
import kuorum.users.KuorumUser
import springSecurity.KuorumRegisterCommand

/**
 * Created by iduetxe on 4/12/15.
 */
@Validateable
class ContactRegister extends KuorumRegisterCommand{
    String cause;
    String message;
    KuorumUser politician

    static constraints = {
//        importFrom KuorumRegisterCommand , include:["name", "email"]
        message nullable: false, minSize: 10
        cause nullable: false
        politician nullable:false
    }
}
