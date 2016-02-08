package kuorum.web.commands.editor

import grails.validation.Validateable
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 8/02/16.
 */
@Validateable
class EditorCreateUserCommand extends EditorAccountCommand{

    static constraints = {
        password nullable: true //Chapu para usar herencia y luego poder ignorar el campo
        user nullable:true //Chapu para usar herencia y luego poder ignorar el campo
        alias nullable: false, validator: {val, obj ->
            if (val && KuorumUser.findByAlias(val)){
                return "unique"
            }
        }
        email nullable: false, email:true, validator: {val, obj ->
            if (val && KuorumUser.findByEmail(val)){
                return "unique"
            }
        }
    }
}
