package kuorum.mail

import grails.validation.Validateable
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

/**
 * Created by iduetxe on 10/02/14.
 */
@Validateable
class MailData {
    KuorumUser user
    MailType mailType
    def bindings = [:]

    static constraints = {
        user nullable: false
        mailType nullable: false
        bindings validator: { val, obj->
            obj.mailType.requiredBindings.each {
                if (!val.containsKey(it)){
                    return "mailData.validator.notFoundBindingRequired.${it}"
                }
            }
            return true
        }
    }
}
