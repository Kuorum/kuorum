package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.users.KuorumUser
import org.grails.databinding.BindUsing

/**
 * Created with IntelliJ IDEA.
 * User: iduetxe
 * Date: 14/08/13
 * Time: 19:28
 * To change this template use File | Settings | File Templates.
 */
@Validateable
class ForgotUserPasswordCommand {
    @BindUsing({
        ForgotUserPasswordCommand cmd, org.grails.databinding.DataBindingSource source ->
            String rawEmail = source['email']?.toLowerCase()
            cmd.user =  KuorumUser.findByEmailAndDomain(rawEmail, CustomDomainResolver.domain)
            rawEmail
    })
    String email

    KuorumUser user


    static constraints = {
        user nullable: false
        email nullable: false, email:true, validator: {val, obj->
            if (val && !obj.user){
                return 'register.forgotPassword.notUserNameExists'
            }
            true
        }
    }

}
