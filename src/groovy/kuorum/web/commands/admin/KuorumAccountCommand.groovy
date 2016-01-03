package kuorum.web.commands.admin

import grails.validation.Validateable
import kuorum.users.KuorumUser

@Validateable
class KuorumAccountCommand {
    KuorumUser user;
    String alias;
    Boolean active;
    static constraints = {
        user nullable:false
        alias nullable:false, validator: {val, obj ->
            if (val && obj.user && val != obj.user.alias && KuorumUser.findByAlias(val)){
                return "notUnique"
            }
        }
    }
}
