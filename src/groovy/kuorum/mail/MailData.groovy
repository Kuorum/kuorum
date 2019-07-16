package kuorum.mail

import grails.validation.Validateable

/**
 * Created by iduetxe on 10/02/14.
 */
@Validateable
class   MailData {
    String fromName
    MailType mailType
    def globalBindings = [:]
    List<MailUserData> userBindings = []

    static constraints = {
        fromName nullable:false, blank: false
        mailType nullable: false
        globalBindings validator: { val, obj->
            obj.mailType.globalBindings.each {
                if (!val.containsKey(it)){
                    return "mailData.validator.notFoundGlobalBindingRequired.${it}"
                }
            }
            return true
        }
        userBindings validator: { val, obj->
            val.each {MailUserData data ->
                obj.mailType.requiredBindings.each {requiredField ->
                    if (!data.bindings.containsKey(requiredField)){
                        return "mailData.validator.notFoundBindingRequired.${it}"
                    }
                }
            }
            return true
        }
    }
}
