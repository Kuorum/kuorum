package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.users.KuorumUser

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
    def user

    static constraints = {
        email nullable: false, email:true, validator: {val, obj->
            KuorumUser user = KuorumUser.findByEmail(val)
            if (obj.email && !user){
                return 'register.forgotPassword.notUserNameExists'
            }
            if (user)
                obj.user = user
            true
        }
    }

}
