package kuorum.web.commands.editor

import grails.validation.Validateable
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand

/**
 * Created by iduetxe on 8/02/16.
 */
@Validateable
class EditorCreateUserCommand extends AccountDetailsCommand{

    static constraints = {
        password nullable: true //Chapu para usar herencia y luego poder ignorar el campo
        user nullable:true //Chapu para usar herencia y luego poder ignorar el campo
        alias nullable: false, maxSize: 15, matches: KuorumUser.ALIAS_REGEX, validator: {val, obj ->
            if (val && KuorumUser.findByAlias(val)){
                return "unique"
            }
        }
        email nullable: false, email:true, validator: {val, obj ->
            if (val && KuorumUser.findByEmailAndDomain(val,CustomDomainResolver.domain)){
                return "unique"
            }
        }
    }
}
