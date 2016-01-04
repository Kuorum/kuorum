package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class AccountDetailsCommand {
    KuorumUser user;
    String alias;
    String email;
    AvailableLanguage language;

    static constraints = {
        user nullable: false
        alias nullable: true, validator: {val, obj ->
            if (val && obj.user && val != obj.user.alias && KuorumUser.findByAlias(val)){
                return "unique"
            }
        }
        email nullable: false, email:true, validator: {val, obj ->
            if (val && obj.user && val != obj.user.alias && KuorumUser.findByAlias(val)){
                return "unique"
            }
        }
        language nullable:false
    }
}
